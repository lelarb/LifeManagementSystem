package app;

import javax.swing.*;

public class MainMenuPanel {
    private JPanel mainPanel;
    private JButton financeButton;

    public MainMenuPanel(AppFrame frame) {
        financeButton.addActionListener(e -> frame.showScreen(AppFrame.FINANCE));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
