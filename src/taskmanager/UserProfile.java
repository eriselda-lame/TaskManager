/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
/**
 *
 * @author User
 */
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String displayName;
    private final String salt;
    private final String passwordHash;
    public UserProfile(String username,
                       String displayName,
                       String password) {
        this.username = username;
        this.displayName = displayName;
        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);
        this.salt = Base64.getEncoder() .encodeToString(saltBytes);
        this.passwordHash = hashPassword(password, saltBytes);
    }
    public String getUsername() {
        return username;
    }
    public String getDisplayName() {
        return displayName;
    }
    public boolean verifyPassword(String password) {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        String enteredHash = hashPassword(password, saltBytes);
        return passwordHash.equals(enteredHash);
    }
    private static String hashPassword(
            String password, byte[] saltBytes) {
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                120000,
                256);
        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder()
                    .encodeToString(hash);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Could not process password.", ex);
        } finally {
            spec.clearPassword();
        }
    }
}
