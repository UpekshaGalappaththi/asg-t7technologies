package io.kfone.store.consumer.utils;

import io.kfone.store.consumer.constants.KfoneStoreConsumerConstants;

/**
 * Exposes the utility functions required for the KfoneStoreConsumerApplication.
 */
public class KfoneStoreConsumerUtils {

    /**
     * Calculates the points earned byt the user after purchasing products.
     *
     * @param points The currently available point count.
     * @param price  The total amount spent on the current purchase.
     * @return The new points value that corresponds to the user.
     */
    public static double calculatePoints(double points, int price) {

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

        if (points < 150) {
            tier = "";
        } else if (points < 300) {
            tier = KfoneStoreConsumerConstants.SILVER_TIER_KEY;
        } else if (points < 500) {
            tier = KfoneStoreConsumerConstants.GOLD_TIER_KEY;
        } else {
            tier = KfoneStoreConsumerConstants.PLATINUM_TIER_KEY;
        }

        return tier;
    }
}
