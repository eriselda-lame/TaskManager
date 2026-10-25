/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author User
 */
public class AppStyle {
   public static final Color PINK_LIGHT=new Color(255,239,245);
    public static final Color PINK =new Color(244,166,193);
    public static final Color PINK_DARK =new Color(177,77,119);
    public static final Color NIGHT =new Color(39,35,47);
    public static final Color NIGHT_PANEL =new Color(54,48,64);
    public static final String ICON_PATH =
        "C:\\Users\\User\\OneDrive\\Desktop\\cartoon-illustration-cute-pink-clipboard-completed-to-do-list-check-marks-organization-cartoon-illustration-400035888.webp";

    private AppStyle() {
    }
    public static Image loadAppIcon(){
        File file=new File(ICON_PATH);
        if (file.exists()){
            return new ImageIcon(ICON_PATH).getImage();
        }
        return new ImageIcon().getImage();
    }
    public static JButton bubbleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD,13));
        button.setForeground(PINK_DARK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(new LineBorder(PINK,2,true),new EmptyBorder(8, 14, 8, 14)));
        return button;
    }  
}
