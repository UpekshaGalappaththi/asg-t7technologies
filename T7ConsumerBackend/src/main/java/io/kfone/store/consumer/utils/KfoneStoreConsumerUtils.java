package io.kfone.store.consumer.utils;

import io.kfone.store.consumer.constants.KfoneStoreConsumerConstants;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Exposes the utility functions required for the KfoneStoreConsumerApplication.
 */
public class KfoneStoreConsumerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(KfoneStoreConsumerUtils.class);

    /**
     * Calculates the points earned byt the user after purchasing products.
     *
     * @param points The currently available point count.
     * @param price  The total amount spent on the current purchase.
     * @return The new points value that corresponds to the user.
     */
    public static double calculatePoints(double points, int price) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Calculating the new point count for the user with the parameters current " +
                    "points: %f and total: %d.", points, price));
        }

        for (int i = 1; i <= price; i++) {
            if (points < 150) {
                points = points + 0.5;
            } else if (points < 300) {
                points = points + 1;
            } else if (points < 500) {
                points = points + 2;
            } else {
                points = points + 3;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Newly calculated point count is: %f.", points));
        }

        return points;
    }

    /**
     * Extract the tier to which the user belongs based on the available point count.
     *
     * @param points The number of points which the user has.
     * @return The tier which the user belongs to.
     */
    public static String getTier(double points) {

        String tier;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Determining the tier of the user.");
        }

        if (points < 150) {
            tier = KfoneStoreConsumerConstants.DEFAULT_TIER_KEY;
        } else if (points < 300) {
            tier = KfoneStoreConsumerConstants.SILVER_TIER_KEY;
        } else if (points < 500) {
            tier = KfoneStoreConsumerConstants.GOLD_TIER_KEY;
        } else {
            tier = KfoneStoreConsumerConstants.PLATINUM_TIER_KEY;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("The user's tier was determined as the %s.", tier));
        }

        return tier;
    }

    /**
     * Calls the SCIM Me endpoint based on the provided parameters and returns the response.
     *
     * @param authHeader  The authorization header.
     * @param httpMethod  The http method which should be used for the SCIM request.
     * @param requestBody The request body.
     * @return The response from the SCIM API.
     */
    public static ResponseEntity<String> callSCIMMeEndpoint(String authHeader, HttpMethod httpMethod,
                                                            String requestBody) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set(KfoneStoreConsumerConstants.CONTENT_TYPE_KEY, KfoneStoreConsumerConstants.CONTENT_TYPE_VALUE);
        headers.set(KfoneStoreConsumerConstants.AUTHORIZATION_KEY, authHeader);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make RestTemplate work with PATCH requests.
        if (httpMethod == HttpMethod.PATCH) {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                    httpClient);
            restTemplate.setRequestFactory(requestFactory);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Sending a SCIM ME %s request with the body: %s", httpMethod, requestBody));
        }

        return restTemplate.exchange(KfoneStoreConsumerConstants.SCIM_ME_API_ENDPOINT, httpMethod, requestEntity,
                String.class);
    }
}
