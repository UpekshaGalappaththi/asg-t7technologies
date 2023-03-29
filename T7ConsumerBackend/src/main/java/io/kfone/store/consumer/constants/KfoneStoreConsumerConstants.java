package io.kfone.store.consumer.constants;

/**
 * Exposes the constants required for the KfoneStoreConsumerApplication.
 */
public class KfoneStoreConsumerConstants {

    public static final String FILE_PATH_DEVICE_INFORMATION = "/devices.json";
    public static final String TOTAL_PARAM_NAME = "total";
    public static final String POINTS_PARAM_NAME = "points";
    public static final String TIER_PLACEHOLDER_KEY = "tier-placeholder";
    public static final String POINTS_PLACEHOLDER_KEY = "points-placeholder";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String SCIM_ME_API_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/scim2/Me";
    public static final String SCIM_WSO2_SCHEMA = "urn:scim:wso2:schema";
    public static final String SCIM_ME_PATCH_REQUEST_BODY =
            """
                      {
                        "schemas": [
                          "urn:ietf:params:scim:api:messages:2.0:PatchOp"
                        ],
                        "Operations": [
                          {
                            "op": "replace",
                            "value": {
                              "urn:scim:wso2:schema": {
                                "tier": "tier-placeholder",
                                "points": "points-placeholder"
                              }
                            }
                          }
                        ]
                      }
                    """;

    public static final String DEFAULT_TIER_KEY = "Default Tier";
    public static final String SILVER_TIER_KEY = "Sliver Tier";
    public static final String GOLD_TIER_KEY = "Gold Tier";
    public static final String PLATINUM_TIER_KEY = "Platinum Tier";
}
