/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 *
 * @author User
 */
public class LoginFrame extends JFrame{
    private final JTextField usernameField=new JTextField(18);
    private final JTextField displayNameField=new JTextField(18);
    private final Map<String, UserProfile> users;
    public LoginFrame() throws IOException {
        users = DataStore.loadUsers();
        setTitle("Task Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440,360);
        setLocationRelativeTo(null);
        setIconImage(AppStyle.loadAppIcon());
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(AppStyle.PINK_LIGHT);
        root.setBorder(BorderFactory.createEmptyBorder(25,30,25,30));
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(AppStyle.PINK, 2, true),
                BorderFactory.createEmptyBorder(22,24,22,24)));
        JLabel title = new JLabel("Task Manager ♡");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(AppStyle.PINK_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(18));
        card.add(new JLabel("Username"));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(10));
        card.add(new JLabel("Display name"));
        card.add(displayNameField);
        card.add(Box.createVerticalStrut(16));
        JButton login = AppStyle.bubbleButton("Log In");
        JButton create = AppStyle.bubbleButton("Create Profile");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        create.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(login);
        card.add(Box.createVerticalStrut(8));
        card.add(create);
        JLabel note = new JLabel("Your profile and tasks stay on this computer.");
        note.setFont(new Font("SansSerif", Font.PLAIN, 10));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(8));
        card.add(note);
        root.add(card);
        setContentPane(root);
        login.addActionListener(e -> logIn());
        create.addActionListener(e -> createProfile());
    }
    private void logIn() {
        String username = usernameField.getText().trim().toLowerCase();
        if (username.isEmpty()) {
            showError("Enter your username.");
            return;
        }
        UserProfile user = users.get(username);
        if (user == null) {
            showError(
                "Profile not found. Create a profile first.");
            return;
        }
        openManager(user);
    }
    private void createProfile() {
        String username = usernameField.getText().trim().toLowerCase();
        String displayName = displayNameField.getText().trim();
        if (username.isEmpty() || displayName.isEmpty()) {
            showError("Enter both username and display name.");
            return;
        }
        if (!username.matches("[a-z0-9_.-]+")) {
            showError("Use lowercase letters, numbers, dot, dash or underscore.");
            return;
        }
        if (users.containsKey(username)) {
            showError(
                "Username already exists. Please log in.");
            return;
        }
        UserProfile user=new UserProfile(username, displayName);
        users.put(username, user);
        try {
            DataStore.saveUsers(users);
            openManager(user);
        } catch (IOException ex) {
            showError(
                "Could not save profile: " + ex.getMessage());
        }
    }
    private void openManager(UserProfile user) {
        try {
            TaskManagerFrame frame=new TaskManagerFrame(user);
            frame.setVisible(true);
            dispose();
        } catch (IOException ex) {
            showError(
                "Could not open tasks: " + ex.getMessage());
        }
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Task Manager",
                JOptionPane.WARNING_MESSAGE);
    }
    
}
