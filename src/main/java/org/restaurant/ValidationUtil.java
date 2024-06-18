package org.restaurant;

public class ValidationUtil {
    public static boolean isValidSize(int size) {
        try {
            Integer.parseInt(String.valueOf(size));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
