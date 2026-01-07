package trackers.water;

import app.AppFrame;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WaterPanel {
    private JPanel mainPanel;
    private JTextField dateField;
    private JTextField amountField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;
    private JTable table;
    private JLabel avgLabel;
    private JLabel streakLabel;
    private final AppFrame frame;
    private final WaterService service = new WaterService();
    private ArrayList<WaterEntry> current = new ArrayList<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public WaterPanel(AppFrame frame) {
        this.frame = frame;

        dateField.setText(LocalDate.now().format(dateFormatter));

        loadTableAndStats();

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && row < current.size()) {
                WaterEntry we = current.get(row);
                dateField.setText(formatForDisplay(we.getDate()));
                amountField.setText(String.valueOf(we.getAmount()));
            }
        });

        addButton.addActionListener(e -> onAdd());
        updateButton.addActionListener(e -> onUpdate());
        deleteButton.addActionListener(e -> onDelete());
        backButton.addActionListener(e -> frame.showScreen(AppFrame.MENU));
    }

    private void onAdd() {
        String username = frame.getCurrentUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(mainPanel, "Niste ulogovani.");
            frame.showScreen(AppFrame.LOGIN);
            return;
        }

        String dateInput = dateField.getText().trim();
        String date = toIsoForDb(dateInput);
        String amountTxt = amountField.getText().trim();

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(mainPanel, "Datum mora biti u formatu yyyy-MM-dd (npr. 2026-01-07).");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountTxt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Količina mora biti broj (ml).");
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(mainPanel, "Količina mora biti veća od 0.");
            return;
        }

        service.add(new WaterEntry(username, date, amount));
        loadTableAndStats();
        amountField.setText("");
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Odaberite unos iz tabele.");
            return;
        }

        String username = frame.getCurrentUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(mainPanel, "Niste ulogovani.");
            frame.showScreen(AppFrame.LOGIN);
            return;
        }

        WaterEntry original = current.get(row);
        ObjectId id = original.getId();

        String dateInput = dateField.getText().trim();
        String date = toIsoForDb(dateInput);
        String amountTxt = amountField.getText().trim();

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(mainPanel, "Datum mora biti u formatu yyyy-MM-dd.");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountTxt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Količina mora biti broj (ml).");
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(mainPanel, "Količina mora biti veća od 0.");
            return;
        }

        service.update(id, username, date, amount);
        loadTableAndStats();
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Odaberite unos iz tabele.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                mainPanel,
                "Jeste li sigurni da želite obrisati unos?",
                "Potvrda",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        String username = frame.getCurrentUsername();
        WaterEntry we = current.get(row);

        service.delete(we.getId(), username);
        loadTableAndStats();
    }

    private void loadTableAndStats() {
        String username = frame.getCurrentUsername();
        if (username == null) {
            current = new ArrayList<>();
        } else {
            current = service.getAllForUser(username);
        }

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Količina (ml)");

        for (WaterEntry we : current) {
            String formatted = formatForDisplay(we.getDate());
            model.addRow(new Object[]{ formatted, we.getAmount() });
        }

        table.setModel(model);

        if (username != null) {
            avgLabel.setText("Prosjek: " + String.format("%.1f", service.getAverageAmount(username)) + " ml");
            streakLabel.setText("Streak: " + service.getStreak(username) + " dana");
        } else {
            avgLabel.setText("Prosjek: 0 ml");
            streakLabel.setText("Streak: 0 dana");
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    private String formatForDisplay(String isoDate) {
        try {
            LocalDate d = LocalDate.parse(isoDate);
            return d.format(dateFormatter);
        } catch (Exception e) {
            return isoDate;
        }
    }

    private String toIsoForDb(String displayDate) {
        try {
            LocalDate d = LocalDate.parse(displayDate, dateFormatter);
            return d.toString();
        } catch (Exception e) {
            return displayDate;
        }
    }


    public void refresh() {
        dateField.setText(LocalDate.now().format(dateFormatter));
        loadTableAndStats();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
