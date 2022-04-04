import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

// @SuppressWarnings("serial")
public class ConfigScreen extends JPanel {
    enum FieldTitle {
        SERVER("Server Name", KeyEvent.VK_S), USER("Username", KeyEvent.VK_U), PASS("Password", KeyEvent.VK_P),
        SAVED("Save Info", KeyEvent.VK_N);

        private String title;
        private int mnemonic;

        private FieldTitle(String title, int mnemonic) {
            this.title = title;
            this.mnemonic = mnemonic;
        }

        public String getTitle() {
            return title;
        }

        public int getMnemonic() {
            return mnemonic;
        }
    }

    public JCheckBox saved;
    private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
    private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);

    private Map<FieldTitle, JTextField> fieldMap = new HashMap<FieldTitle, JTextField>();

    public ConfigScreen() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Server Login"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        GridBagConstraints gbc;

        for (int i = 0; i < FieldTitle.values().length; i++) {
            FieldTitle fieldTitle = FieldTitle.values()[i];
            JLabel label = new JLabel(fieldTitle.getTitle() + ":", JLabel.LEFT);
            if (i == 2) {
                JPasswordField textField = new JPasswordField();
                label.setLabelFor(textField);
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(textField, gbc);

                fieldMap.put(fieldTitle, textField);
            } else if (i == 0 || i == 1) {
                JTextField textField = new JTextField();
                label.setLabelFor(textField);
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(textField, gbc);

                fieldMap.put(fieldTitle, textField);
            } else {
                saved = new JCheckBox();
                label.setLabelFor(saved);
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(saved, gbc);
            }
        }
    }

    public ConfigScreen(ArrayList<String> values) {
        // JMenuBar menuBar = new JMenuBar();
        // JMenu menu = new JMenu("Connection");
        // JMenuItem menuItem = new JMenuItem("Use Saved Connetion");
        // menu.add(menuItem);
        // menuBar.add(menu);
        // add(menuBar, BorderLayout.PAGE_START);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Server Login"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        GridBagConstraints gbc;

        for (int i = 0; i < FieldTitle.values().length; i++) {
            FieldTitle fieldTitle = FieldTitle.values()[i];
            JLabel label = new JLabel(fieldTitle.getTitle() + ":", JLabel.LEFT);
            if (i == 2) {
                JPasswordField textField = new JPasswordField();
                label.setLabelFor(textField);
                textField.setText(values.get(2));
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(textField, gbc);

                fieldMap.put(fieldTitle, textField);
            } else if (i == 0) {
                JTextField textField = new JTextField();
                textField.setText(values.get(0));
                label.setLabelFor(textField);
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(textField, gbc);

                fieldMap.put(fieldTitle, textField);
            } else if (i == 1) {
                JTextField textField = new JTextField();
                textField.setText(values.get(1));
                label.setLabelFor(textField);
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(textField, gbc);

                fieldMap.put(fieldTitle, textField);
            } else {
                saved = new JCheckBox();
                saved.setSelected(true);
                label.setLabelFor(saved);
                gbc = createGbc(0, i);
                add(label, gbc);
                gbc = createGbc(1, i);
                add(saved, gbc);
            }
        }
    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        gbc.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;

        gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
        gbc.weightx = (x == 0) ? 0.1 : 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    public String getFieldText(FieldTitle fieldTitle) {
        return fieldMap.get(fieldTitle).getText();
    }

}
