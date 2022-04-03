import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.*;

public class App {
    private static Connection conn;
    private static Statement statement;
    private static ConfigScreen configScreen;
    private static TextEditorScreen textEditorScreen;

    public static void main(String[] args) throws Exception {
        // start JFrame app here
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        ArrayList<String> values = new ArrayList<String>();
        configScreen = new ConfigScreen();

        int result = JOptionPane.showConfirmDialog(null, configScreen, "JDBC Executor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // do something with info

            for (ConfigScreen.FieldTitle fieldTitle : ConfigScreen.FieldTitle.values()) {
                // System.out.printf("%10s: %s%n", fieldTitle.getTitle(),
                // configScreen.getFieldText(fieldTitle));
                values.add(configScreen.getFieldText(fieldTitle));
            }

            String url = "jdbc:sqlserver://" + values.get(0) + ";"
                    + "database=TSQLV4;"
                    + "user=" + values.get(1) + ";"
                    + "password=" + values.get(2) + ";"
                    + "encrypt=true;"
                    + "trustServerCertificate=True;"
                    + "hostNameInCertificate=*.";
            try {
                conn = DriverManager.getConnection(url);
                statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                configScreen.setEnabled(false);
                System.out.println("logged in successfully");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong with your query error:\n" + e.toString(),
                        "JDBC Executor",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
}
