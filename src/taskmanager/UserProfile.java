/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class UserProfile implements Serializable {
    private static final long serialVersionUID=1L;
    private final String username;
    private final String displayName;
    public UserProfile(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }
    public String getUsername() {
        return username;
    }
    public String getDisplayName() {
        return displayName;
    }
}
