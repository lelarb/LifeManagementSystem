package app;

import auth.LoginPanel;
import auth.RegisterPanel;
import financeapp.FinanceTrackerForm;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame{
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String MENU = "menu";
    public static final String FINANCE = "finance";
    public static final String HOME = "home";

    private final CardLayout layout = new CardLayout();
    private final JPanel root = new JPanel(layout);

    public AppFrame(){
        super("Life Management System");

        LoginPanel login = new LoginPanel(this);
        RegisterPanel register = new RegisterPanel(this);
        HomePanel home = new HomePanel(this);
        MainMenuPanel menu = new MainMenuPanel(this);
        FinanceTrackerForm finance = new FinanceTrackerForm(this);


        root.add(login.getMainPanel(), LOGIN);
        root.add(register.getMainPanel(), REGISTER);
        root.add(home.getMainPanel(), HOME);
        root.add(menu.getMainPanel(), MENU);
        root.add(finance.getMainPanel(), FINANCE);

        setContentPane(root);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        showScreen(LOGIN);
    }

    public void showScreen(String name){
        layout.show(root, name);
    }
}
