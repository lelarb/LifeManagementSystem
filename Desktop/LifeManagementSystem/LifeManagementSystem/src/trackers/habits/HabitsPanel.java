package trackers.habits;

import app.AppFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HabitsPanel {
    private JPanel mainPanel;
    private JTextField dateField;
    private JCheckBox doneCheckBox;
    private JTextField noteField;
    private JButton saveButton;
    private JButton backButton;
    private JTable table;

    private final AppFrame frame;
    private final HabitService service = new HabitService();
    private ArrayList<HabitEntry> current = new ArrayList<>();

    private final DateTimeFormatter displayFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public HabitsPanel(AppFrame frame) {
        this.frame = frame;

        dateField.setText(LocalDate.now().format(displayFmt));

        loadTable();

        saveButton.addActionListener(e -> onSave());
        backButton.addActionListener(e -> frame.showScreen(AppFrame.MENU));
    }

    private void onSave() {
        String username = frame.getCurrentUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(mainPanel, "Niste ulogovani.");
            frame.showScreen(AppFrame.LOGIN);
            return;
        }

        String displayDate = dateField.getText().trim();
        String isoDate;
        try {
            isoDate = LocalDate.parse(displayDate, displayFmt).toString();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Datum mora biti dd.MM.yyyy (npr. 08.01.2026).");
            return;
        }

        boolean done = doneCheckBox.isSelected();
        String note = noteField.getText().trim();

        service.save(username, isoDate, done, note);
        loadTable();

        noteField.setText("");
        doneCheckBox.setSelected(false);
    }

    private void loadTable() {
        String username = frame.getCurrentUsername();
        current = (username == null) ? new ArrayList<>() : service.getAllForUser(username);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Urađeno");
        model.addColumn("Napomena");

        for (HabitEntry he : current) {
            String displayDate = he.getDate();
            try {
                displayDate = LocalDate.parse(he.getDate()).format(displayFmt);
            } catch (Exception ignored) {}

            model.addRow(new Object[]{
                    displayDate,
                    he.isDone() ? "DA" : "NE",
                    he.getNote()
            });
        }

        table.setModel(model);
    }

    public void refresh() {
        dateField.setText(LocalDate.now().format(displayFmt));
        doneCheckBox.setSelected(false);
        noteField.setText("");
        loadTable();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

