package app;

import javax.swing.*;

public class HomePanel {
    private JPanel mainPanel;
    private JButton viewProfileButton;
    private JButton financeButton;
    private JButton myTrackersButton;

    public HomePanel(AppFrame frame) {

        viewProfileButton.addActionListener(e -> frame.showProfile());

        financeButton.addActionListener(e -> frame.showScreen(AppFrame.FINANCE));

        myTrackersButton.addActionListener(e -> frame.showScreen(AppFrame.MENU));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

