package app;

import auth.LoginPanel;
import auth.RegisterPanel;
import financeapp.FinanceTrackerForm;
import trackers.water.WaterPanel;
import trackers.sleep.SleepPanel;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String MENU = "menu";
    public static final String FINANCE = "finance";
    public static final String HOME = "home";
    public static final String PROFILE = "profile";
    public static final String WATER = "water";
    public static final String SLEEP = "sleep";

    private final CardLayout layout = new CardLayout();
    private final JPanel root = new JPanel(layout);
    private String currentUsername;
    private ProfilePanel profilePanel;
    private FinanceTrackerForm financePanel;
    private String currentTheme = "Default";
    private WaterPanel waterPanel;
    private SleepPanel sleepPanel;



    public AppFrame() {
        super("Life Management System");

        LoginPanel login = new LoginPanel(this);
        RegisterPanel register = new RegisterPanel(this);
        HomePanel home = new HomePanel(this);
        MainMenuPanel menu = new MainMenuPanel(this);
        financePanel = new FinanceTrackerForm(this);
        profilePanel = new ProfilePanel(this);
        WaterPanel waterPanel = new WaterPanel(this);
        sleepPanel = new SleepPanel(this);

        root.add(login.getMainPanel(), LOGIN);
        root.add(register.getMainPanel(), REGISTER);
        root.add(home.getMainPanel(), HOME);
        root.add(menu.getMainPanel(), MENU);
        root.add(financePanel.getMainPanel(), FINANCE);
        root.add(profilePanel.getMainPanel(), PROFILE);
        root.add(waterPanel.getMainPanel(), WATER);
        root.add(sleepPanel.getMainPanel(), SLEEP);


        setContentPane(root);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        showScreen(LOGIN);
    }

    public void showScreen(String name) {
        layout.show(root, name);
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void showProfile() {
        profilePanel.refresh();
        showScreen(PROFILE);
    }

    public void logout() {
        setCurrentUsername(null);
        showScreen(LOGIN);
        applyTheme("Default");
    }

    public void setTheme(String theme) {
        if (theme == null || theme.isBlank()) theme = "Default";
        currentTheme = theme;
    }

    public String getTheme() {
        return currentTheme;
    }

    public void applyTheme(String theme) {
        setTheme(theme);

        java.awt.Color bg;
        java.awt.Color fg;

        switch (currentTheme) {
            case "Blue":
                bg = new java.awt.Color(200, 220, 255);
                fg = java.awt.Color.BLACK;
                break;
            case "Green":
                bg = new java.awt.Color(210, 240, 210);
                fg = java.awt.Color.BLACK;
                break;
            case "Pink":
                bg = new java.awt.Color(255, 220, 235);
                fg = java.awt.Color.BLACK;
                break;
            case "Dark":
                bg = new java.awt.Color(60, 60, 60);
                fg = java.awt.Color.WHITE;
                break;
            default:
                bg = new java.awt.Color(240, 240, 200);
                fg = java.awt.Color.BLACK;
        }

        applyColorsRecursively(root, bg, fg);
        root.revalidate();
        root.repaint();
    }

    private void applyColorsRecursively(java.awt.Component comp, java.awt.Color bg, java.awt.Color fg) {

        if (comp instanceof javax.swing.JComponent) {
            javax.swing.JComponent jc = (javax.swing.JComponent) comp;

            jc.setOpaque(true);
            jc.setBackground(bg);

            boolean isInput =
                    (jc instanceof javax.swing.JTextField) ||
                            (jc instanceof javax.swing.JPasswordField) ||
                            (jc instanceof javax.swing.JComboBox) ||
                            (jc instanceof javax.swing.JTable) ||
                            (jc instanceof javax.swing.JTextArea);

            if (isInput) {
                if ("Dark".equals(currentTheme)) {
                    jc.setBackground(new java.awt.Color(90, 90, 90));
                    jc.setForeground(java.awt.Color.WHITE);
                } else {
                    jc.setForeground(java.awt.Color.BLACK);
                }
            } else {
                jc.setForeground(fg);
            }
        }

        if (comp instanceof java.awt.Container) {
            java.awt.Container cont = (java.awt.Container) comp;
            for (java.awt.Component child : cont.getComponents()) {
                applyColorsRecursively(child, bg, fg);
            }
        }
    }

    public void showWater() {
        if (waterPanel == null) {
            waterPanel = new WaterPanel(this);
            root.add(waterPanel.getMainPanel(), WATER);
        }
        waterPanel.refresh();
        showScreen(WATER);
    }

    public void showSleep() {
        sleepPanel.refresh();
        showScreen(SLEEP);
    }

    public void showFinance(){
        financePanel.refresh();
        showScreen(FINANCE);
    }

}


