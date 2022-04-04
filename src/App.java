import java.sql.Statement;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class App {
    private static Connection conn;
    private static Statement statement;
    private static ConfigScreen configScreen;

    public static void main(String[] args) throws Exception {
        // start JFrame app here
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static ArrayList<String> checkSaved() {
        String values = "none";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("./src/savedConfigs.csv"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                values = line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] valuesArr = values.split(",");
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, valuesArr);
        // if (list.size() > 1) {
        // list.remove(0);
        // }
        return list;
    }

    private static void createAndShowGUI() {
        ArrayList<String> values;
        values = checkSaved();
        boolean loadSuccess = values.size() == 1 ? false : true;
        configScreen = values.size() == 1 ? new ConfigScreen() : new ConfigScreen(values);
        int result = JOptionPane.showConfirmDialog(null, configScreen, "JDBC Executor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (!loadSuccess) {
                for (ConfigScreen.FieldTitle fieldTitle : ConfigScreen.FieldTitle.values()) {
                    if (fieldTitle.getTitle() == "Save Info") {
                        if (configScreen.saved.isSelected()) {
                            values.add("isSaved");
                        } else {
                            values.add("notSaved");
                        }
                    } else {
                        values.add(configScreen.getFieldText(fieldTitle));
                    }
                }
            } else {
                for (ConfigScreen.FieldTitle fieldTitle : ConfigScreen.FieldTitle.values()) {
                    if (fieldTitle.getTitle() == "Save Info") {
                        if (configScreen.saved.isSelected()) {
                            values.add("isSaved");
                        } else {
                            values.add("notSaved");
                        }
                    }
                }
            }

            if (values.get(0) == "none") {
                values.remove(0);
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
                statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                System.out.println(values);
                Boolean saveChecked = values.get(3) == "isSaved" ? true : false;
                if (saveChecked == true && loadSuccess == false) {
                    String completeValues = values.get(0) + "," + values.get(1) + "," + values.get(2);
                    saveInformation(completeValues);
                } else if (saveChecked == true && loadSuccess == true) {
                    // do nothing
                } else {
                    clearCSV();
                }

                configScreen.setEnabled(false);
                new TextEditorScreen(statement, conn);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong with your login:\n"
                        + e.toString(),
                        "JDBC Executor",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        if (result == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }
    }

    public static void saveInformation(String values) {
        clearCSV();
        File file = new File("./src/savedConfigs.csv");
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(file);
            myWriter.write(values);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearCSV() {
        File file = new File("./src/savedConfigs.csv");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}