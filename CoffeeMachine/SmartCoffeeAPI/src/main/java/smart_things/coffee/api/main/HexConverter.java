package smart_things.coffee.api.main;

/**
 * Contains static methods to handle hexadecimal calculations. Provides methods
 * to convert an ASCII string into a hex string and vice versa.
 *
 * @author Jan
 * @version 1.01
 */
public class HexConverter {

    /**
     * Converts an ASCII string into a hexadecimal string.
     *
     * @param ascii The ASCII string to convert
     * @return The hexadecimal representation of the given ASCII string
     */
    public static String encodeHex(String ascii) {
        char[] chars = ascii.toCharArray();
        // Create a buffer for the hex string
        StringBuffer hex = new StringBuffer();
        // Convert it char by char into hex
        for (int i = 0; i < chars.length; i++) {
            hex.append(encodeHex((int) chars[i]));
        }
        return hex.toString();
    }

    /**
     * Convert an integer into a two-digit hexadecimal lower case string. A
     * leading zero will be prepended, if length of hexadecimal representation
     * is one.
     *
     * @param number The integer to convert. It is not allowed to have a hexadecimal representation
     *               that is longer than two digits.
     * @return The hexadecimal representation of the given number
     */
    public static String encodeHex(int number) {
        String hex = Integer.toHexString(number);
        // Illegal length of hexadecimal representation?
        if (hex.length() < 1 || hex.length() > 2) {
            throw new IllegalArgumentException("Hexadecimal representation of the given integer (" + number
                    + ") is too long. Maximum is two digits.");
            // Need to prepend a zero?
        } else if (hex.length() == 1) {
            hex = "0" + hex;
        }
        // Convert to lower case and return
        return hex.toLowerCase();
    }

    /**
     * Converts a hexadecimal string into an ASCII string.
     *
     * @param hex The hexadecimal string to convert
     * @return The ASCII representation of the given hexadecimal string
     */
    public static String decodeHex(String hex) {
        // StringBuilder for concatenating
        StringBuilder output = new StringBuilder();
        // Split hex string and iterate
        for (int i = 0; i < hex.length(); i += 2) {
            // Convert and append ASCII
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
}
