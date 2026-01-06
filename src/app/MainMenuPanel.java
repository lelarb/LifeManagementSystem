package app;

import javax.swing.*;

public class MainMenuPanel {
    private JPanel mainPanel;
    private JButton financeButton;
    private JButton backButton;

    public MainMenuPanel(AppFrame frame) {
        financeButton.addActionListener(e -> frame.showScreen(AppFrame.FINANCE));
        backButton.addActionListener(e -> frame.showScreen(AppFrame.HOME));

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
