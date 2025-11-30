package fr.unice.polytech.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class to generate ETag values for HTTP responses
 */
public class ETagGenerator {
    
    /**
     * Generate an ETag from content using MD5 hash
     * 
     * @param content The content to hash
     * @return ETag in format "abc123def456..."
     */
    public static String generateETag(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(content.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            // Return ETag wrapped in quotes (HTTP standard)
            return "\"" + hexString.toString() + "\"";
            
        } catch (NoSuchAlgorithmException e) {
            // Fallback: use hashCode if MD5 not available
            return "\"" + Integer.toHexString(content.hashCode()) + "\"";
        }
    }
}