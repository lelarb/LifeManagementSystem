package auth;

import app.AppFrame;
import org.bson.Document;

import javax.swing.*;

public class LoginPanel {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private final UserService userService = new UserService();

    public LoginPanel(AppFrame frame) {

        loginButton.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Popunite username i password.");
                return;
            }

            if (userService.login(u, p)) {
                frame.setCurrentUsername(u);
                Document user = userService.getUserByUsername(u);
                String theme = (user != null) ? user.getString("theme") : "Default";
                frame.applyTheme(theme);
                frame.showScreen(AppFrame.HOME);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Pogrešan username ili password.");
            }
        });

        registerButton.addActionListener(e -> frame.showScreen(AppFrame.REGISTER));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
