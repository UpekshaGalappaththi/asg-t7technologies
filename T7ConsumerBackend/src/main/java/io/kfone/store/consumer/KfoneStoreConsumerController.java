package io.kfone.store.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kfone.store.consumer.constants.KfoneStoreConsumerConstants;
import io.kfone.store.consumer.utils.KfoneStoreConsumerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("The total value extracted from the request body is %d.", total));
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The request body did not contain the total parameter hence returning a response stating" +
                        " that this is a bad request.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not find the total parameter in the body.");
        }

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body(
                "User was not updated since the total was insufficient ot the value sent as total was not in a proper" +
                        " format.");

        if (total > 0) {

            // Send an SCIM Me API call to get the user's point count.
            if (StringUtils.isBlank(authHeader)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The request did not contain the auth header hence returning a response stating that" +
                            " this is a bad request.");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication header was not found in the " +
                        "request.");
            }

            responseEntity = KfoneStoreConsumerUtils.callSCIMMeEndpoint(authHeader, HttpMethod.GET, null);

            // Extract the current points value.
            ObjectMapper objectMapper = new ObjectMapper();

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("The SCIM ME GET API request responded with the status code %s instead" +
                            " of the status code %s hence returning a response stating that there is an " +
                            "internal server error.", responseEntity.getStatusCode(), HttpStatus.OK));
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while " +
                        "extracting the user details.");
            }

            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(responseEntity.getBody());
            } catch (JsonProcessingException exception) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("There was an error while processing the SCIM ME GET API response body.", exception);
                }
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

            responseEntity = KfoneStoreConsumerUtils.callSCIMMeEndpoint(authHeader, HttpMethod.PATCH, scimRequestBody);
        }

        return responseEntity;
    }
}
