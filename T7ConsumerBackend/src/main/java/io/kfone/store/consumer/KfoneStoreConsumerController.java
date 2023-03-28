package io.kfone.store.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kfone.store.consumer.constants.KfoneStoreConsumerConstants;
import io.kfone.store.consumer.utils.KfoneStoreConsumerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Controller
public class KfoneStoreConsumerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(KfoneStoreConsumerController.class);

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/devices")
    @ResponseBody
    public ResponseEntity<String> devices() {

        JsonNode jsonNode = null;
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getResourceAsStream(
                KfoneStoreConsumerConstants.FILE_PATH_DEVICE_INFORMATION)) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                jsonNode = mapper.readTree(reader);
            }
        } catch (IOException exception) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("An error occurred while reading the device information from the JSON file.", exception);
            }
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }

        if (jsonNode != null) {
            return ResponseEntity.status(HttpStatus.OK).body(jsonNode.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while generating " +
                    "the response.");
        }
    }

    @PostMapping("/purchase")
    @ResponseBody
    public ResponseEntity<String> purchaseOrder(@RequestHeader(KfoneStoreConsumerConstants.AUTHORIZATION_KEY)
                                                String authHeader, @RequestBody JsonNode requestBody) {

        // Extract the total purchase value from the request.
        int total = 0;
        if (requestBody.has(KfoneStoreConsumerConstants.TOTAL_PARAM_NAME)) {
            if (StringUtils.isNumeric(requestBody.get(KfoneStoreConsumerConstants.TOTAL_PARAM_NAME).toString())) {
                total = requestBody.get(KfoneStoreConsumerConstants.TOTAL_PARAM_NAME).asInt();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not find the total parameter in the body.");
        }

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body(
                "User not updated since the total was insufficient.");

        if (total > 0) {

            // Send an SCIM Me API call to get the user's point count.
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set(KfoneStoreConsumerConstants.CONTENT_TYPE_KEY, KfoneStoreConsumerConstants.CONTENT_TYPE_VALUE);
            headers.set(KfoneStoreConsumerConstants.AUTHORIZATION_KEY, authHeader);

            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            responseEntity = restTemplate.exchange(KfoneStoreConsumerConstants.SCIM_ME_API_ENDPOINT, HttpMethod.GET,
                    requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while " +
                        "extracting the user details.");
            }

            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(responseEntity.getBody());
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while " +
                        "reading the user details.");
            }

            double points = 0;
            if (jsonNode.has(KfoneStoreConsumerConstants.SCIM_WSO2_SCHEMA)) {
                if (jsonNode.get(KfoneStoreConsumerConstants.SCIM_WSO2_SCHEMA).has(
                        KfoneStoreConsumerConstants.POINTS_PARAM_NAME)) {
                    points = jsonNode.get(KfoneStoreConsumerConstants.SCIM_WSO2_SCHEMA).get(
                            KfoneStoreConsumerConstants.POINTS_PARAM_NAME).asDouble();
                }
            }

            // Calculate the user's points based on the tier and the current point count.
            points = KfoneStoreConsumerUtils.calculatePoints(points, total);
            String tier = KfoneStoreConsumerUtils.getTier(points);

            // Update the tier/points of the user based on the new point count with an SCIM Me API call.
            String scimRequestBody = KfoneStoreConsumerConstants.SCIM_ME_PATCH_REQUEST_BODY.replace(
                    KfoneStoreConsumerConstants.TIER_PLACEHOLDER_KEY, tier).replace(
                    KfoneStoreConsumerConstants.POINTS_PLACEHOLDER_KEY, String.valueOf(points));

            requestEntity = new HttpEntity<>(scimRequestBody, headers);

            // Make RestTemplate work with PATCH requests.
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                    httpClient);
            restTemplate.setRequestFactory(requestFactory);

            responseEntity = restTemplate.exchange(KfoneStoreConsumerConstants.SCIM_ME_API_ENDPOINT, HttpMethod.PATCH,
                    requestEntity, String.class);
        }

        return responseEntity;
    }
}
