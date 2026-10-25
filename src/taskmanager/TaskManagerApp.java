/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author User
 */
public class TaskManagerApp {
     
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not start application: "
                    + ex.getMessage()
                );
            }
        });
    }
}
