package auth;

import app.AppFrame;

import javax.swing.*;

public class RegisterPanel {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton createButton;
    private JButton backButton;

    private final UserService userService = new UserService();

    public RegisterPanel(AppFrame frame) {

        createButton.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Popunite username i password.");
                return;
            }

            if (userService.register(u, p)) {
                JOptionPane.showMessageDialog(mainPanel, "Registracija uspješna! Uloguj se.");
                frame.showScreen(AppFrame.LOGIN);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Username već postoji!");
            }
        });

        backButton.addActionListener(e -> frame.showScreen(AppFrame.LOGIN));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
