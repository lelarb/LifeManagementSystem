package auth;

import app.AppFrame;
import javax.swing.*;

public class RegisterPanel {
    private JPanel nPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton createButton;
    private JButton backButton;
    private JPanel mainPanel;

    private final UserService userService = new UserService();
    private final AppFrame frame;

    public RegisterPanel(AppFrame frame) {
        this.frame = frame;

        createButton.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(nPanel, "Popunite username i password.");
                return;
            }

            if (userService.register(u, p)) {
                JOptionPane.showMessageDialog(nPanel, "Registracija uspješna! Uloguj se.");
                this.frame.showScreen(AppFrame.LOGIN);
            } else {
                JOptionPane.showMessageDialog(nPanel, "Username već postoji!");
            }
        });

        backButton.addActionListener(e -> this.frame.showScreen(AppFrame.LOGIN));
    }

    public JPanel getMainPanel() {
        return nPanel;
    }
}
