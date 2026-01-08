package trackers.sleep;

import app.AppFrame;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SleepPanel {
    private JPanel mainPanel;
    private JTextField dateField;
    private JTextField sleepField;
    private JTextField wakeField;
    private JComboBox<String> qualityCombo;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;
    private JTable table;
    private JLabel avgHoursLabel;
    private JLabel avgQualityLabel;
    private JLabel streakLabel;

    private final AppFrame frame;
    private final SleepService service = new SleepService();
    private ArrayList<SleepEntry> current = new ArrayList<>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public SleepPanel(AppFrame frame) {
        this.frame = frame;

        dateField.setText(LocalDate.now().format(dateFormatter));
        qualityCombo.setModel(new DefaultComboBoxModel<>(new String[]{"1","2","3","4","5"}));
        qualityCombo.setSelectedIndex(2);

        loadTableAndStats();

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && row < current.size()) {
                SleepEntry se = current.get(row);
                dateField.setText(formatForDisplay(se.getDate()));
                sleepField.setText(se.getSleepTime());
                wakeField.setText(se.getWakeTime());
                qualityCombo.setSelectedItem(String.valueOf(se.getQuality()));
            }
        });

        addButton.addActionListener(e -> onAdd());
        updateButton.addActionListener(e -> onUpdate());
        deleteButton.addActionListener(e -> onDelete());
        backButton.addActionListener(e -> frame.showScreen(AppFrame.MENU));
    }

    public void refresh() {
        dateField.setText(LocalDate.now().format(dateFormatter));
        loadTableAndStats();
    }

    private void onAdd() {
        String username = frame.getCurrentUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(mainPanel, "Niste ulogovani.");
            frame.showScreen(AppFrame.LOGIN);
            return;
        }

        String isoDate = toIsoForDb(dateField.getText().trim());
        if (!isValidIsoDate(isoDate)) {
            JOptionPane.showMessageDialog(mainPanel, "Datum mora biti u formatu dd.MM.yyyy (npr. 07.01.2026).");
            return;
        }

        String sleepTime = sleepField.getText().trim();
        String wakeTime = wakeField.getText().trim();

        if (!isValidTime(sleepTime) || !isValidTime(wakeTime)) {
            JOptionPane.showMessageDialog(mainPanel, "Vrijeme mora biti u formatu HH:mm (npr. 23:30).");
            return;
        }

        int quality = Integer.parseInt((String) qualityCombo.getSelectedItem());
        double hours = calculateHours(sleepTime, wakeTime);

        service.add(new SleepEntry(username, isoDate, sleepTime, wakeTime, hours, quality));
        loadTableAndStats();
        sleepField.setText("");
        wakeField.setText("");
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Odaberite unos iz tabele.");
            return;
        }

        String username = frame.getCurrentUsername();
        SleepEntry original = current.get(row);
        ObjectId id = original.getId();

        String isoDate = toIsoForDb(dateField.getText().trim());
        if (!isValidIsoDate(isoDate)) {
            JOptionPane.showMessageDialog(mainPanel, "Datum mora biti u formatu dd.MM.yyyy.");
            return;
        }

        String sleepTime = sleepField.getText().trim();
        String wakeTime = wakeField.getText().trim();

        if (!isValidTime(sleepTime) || !isValidTime(wakeTime)) {
            JOptionPane.showMessageDialog(mainPanel, "Vrijeme mora biti u formatu HH:mm.");
            return;
        }

        int quality = Integer.parseInt((String) qualityCombo.getSelectedItem());
        double hours = calculateHours(sleepTime, wakeTime);

        service.update(id, username, isoDate, sleepTime, wakeTime, hours, quality);
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
        SleepEntry se = current.get(row);
        service.delete(se.getId(), username);
        loadTableAndStats();
    }

    private void loadTableAndStats() {
        String username = frame.getCurrentUsername();
        current = (username == null) ? new ArrayList<>() : service.getAllForUser(username);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Spavanje");
        model.addColumn("Buđenje");
        model.addColumn("Sati");
        model.addColumn("Kvalitet");

        for (SleepEntry se : current) {
            model.addRow(new Object[]{
                    formatForDisplay(se.getDate()),
                    se.getSleepTime(),
                    se.getWakeTime(),
                    String.format("%.2f", se.getHours()),
                    se.getQuality()
            });
        }

        table.setModel(model);

        if (username != null) {
            avgHoursLabel.setText("Prosjek sati: " + String.format("%.2f", service.getAverageHours(username)));
            avgQualityLabel.setText("Prosjek kvaliteta: " + String.format("%.1f", service.getAverageQuality(username)));
            streakLabel.setText("Streak: " + service.getStreak(username) + " dana");
        } else {
            avgHoursLabel.setText("Prosjek sati: 0");
            avgQualityLabel.setText("Prosjek kvaliteta: 0");
            streakLabel.setText("Streak: 0 dana");
        }
    }

    private boolean isValidTime(String t) {
        try { LocalTime.parse(t); return true; }
        catch (Exception e) { return false; }
    }

    private boolean isValidIsoDate(String iso) {
        try { LocalDate.parse(iso); return true; }
        catch (Exception e) { return false; }
    }

    private String toIsoForDb(String displayDate) {
        try {
            LocalDate d = LocalDate.parse(displayDate, dateFormatter);
            return d.toString();
        } catch (Exception e) {
            return displayDate;
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

    private double calculateHours(String sleepTime, String wakeTime) {
        LocalTime sleep = LocalTime.parse(sleepTime);
        LocalTime wake = LocalTime.parse(wakeTime);

        long minutes = Duration.between(sleep, wake).toMinutes();

        if (minutes <= 0) {
            minutes += 24 * 60;
        }

        return minutes / 60.0;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}

