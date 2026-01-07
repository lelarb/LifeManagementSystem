package app;

import auth.UserService;
import org.bson.Document;
import javax.swing.*;

public class ProfilePanel {
    private JPanel mainPanel;
    private JLabel usernameValueLabel;
    private JButton backButton;
    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox themeComboBox;
    private JButton editButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton logoutButton;

    private final AppFrame frame;
    private final UserService userService = new UserService();

    public ProfilePanel(AppFrame frame) {
        this.frame = frame;

        setEditMode(false);

        if (themeComboBox.getItemCount() == 0) {
            themeComboBox.addItem("Default");
            themeComboBox.addItem("Blue");
            themeComboBox.addItem("Green");
            themeComboBox.addItem("Pink");
            themeComboBox.addItem("Dark");
        }

        editButton.addActionListener(e -> setEditMode(true));

        saveButton.addActionListener(e -> onSave());

        logoutButton.addActionListener(e -> frame.logout());

        deleteButton.addActionListener(e -> onDeleteAccount());

        backButton.addActionListener(e -> frame.showScreen(AppFrame.HOME));
    }

    public void refresh() {
        String u = frame.getCurrentUsername();
        if (u == null || u.isEmpty()) {
            usernameValueLabel.setText("(nema korisnika)");
            setEditMode(false);
            return;
        }

        Document user = userService.getUserByUsername(u);
        usernameValueLabel.setText(u);

        if (user != null) {
            fullNameField.setText(user.getString("fullName"));
            emailField.setText(user.getString("email"));

            String theme = user.getString("theme");
            if (theme == null || theme.isEmpty()) theme = "Default";
            themeComboBox.setSelectedItem(theme);
            applyTheme(theme);
        } else {
            fullNameField.setText("");
            emailField.setText("");
            themeComboBox.setSelectedItem("Default");
        }

        setEditMode(false);
    }

    private void onSave() {
        String u = frame.getCurrentUsername();
        if (u == null) {
            JOptionPane.showMessageDialog(mainPanel, "Nema ulogovanog korisnika.");
            return;
        }

        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String theme = (String) themeComboBox.getSelectedItem();
        if (!email.isEmpty() && !email.contains("@")) {
            JOptionPane.showMessageDialog(mainPanel, "Email nije validan.");
            return;
        }

        boolean ok = userService.updateProfile(u, fullName, email, theme);
        if (ok) {
            JOptionPane.showMessageDialog(mainPanel, "Profil je sačuvan.");
            applyTheme(theme);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Nema promjena ili greška pri spremanju.");
        }

        setEditMode(false);
    }

    private void onDeleteAccount() {
        String u = frame.getCurrentUsername();
        if (u == null) return;

        int res = JOptionPane.showConfirmDialog(
                mainPanel,
                "Jeste li sigurni da želite obrisati račun?\nOva akcija je nepovratna.",
                "Potvrda brisanja",
                JOptionPane.YES_NO_OPTION
        );

        if (res == JOptionPane.YES_OPTION) {
            boolean ok = userService.deleteByUsername(u);
            if (ok) {
                JOptionPane.showMessageDialog(mainPanel, "Račun je obrisan.");
                frame.logout();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Greška: račun nije obrisan.");
            }
        }
    }

    private void setEditMode(boolean editable) {
        fullNameField.setEditable(editable);
        emailField.setEditable(editable);
        themeComboBox.setEnabled(editable);

        editButton.setEnabled(!editable);
        saveButton.setEnabled(editable);
    }

    private void applyTheme(String theme) {
        switch (theme) {
            case "Blue":
                mainPanel.setBackground(new java.awt.Color(200, 220, 255));
                break;
            case "Green":
                mainPanel.setBackground(new java.awt.Color(210, 240, 210));
                break;
            case "Pink":
                mainPanel.setBackground(new java.awt.Color(255, 220, 235));
                break;
            case "Dark":
                mainPanel.setBackground(new java.awt.Color(60, 60, 60));
                break;
            default:
                mainPanel.setBackground(new java.awt.Color(240, 240, 200)); // Default
        }
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}

