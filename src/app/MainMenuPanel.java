package app;

import javax.swing.*;

public class MainMenuPanel {
    private JPanel mainPanel;
    private JButton financeButton;
    private JButton backButton;
    private JButton waterButton;
    private JButton sleepButton;
    private JButton habitsButton;

    public MainMenuPanel(AppFrame frame) {
        financeButton.addActionListener(e -> frame.showScreen(AppFrame.FINANCE));
        backButton.addActionListener(e -> frame.showScreen(AppFrame.HOME));
        waterButton.addActionListener(e -> frame.showWater());
        sleepButton.addActionListener(e -> frame.showSleep());
        habitsButton.addActionListener( e-> frame.showHabits());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
