import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.plaf.metal.*;

class TextEditorScreen extends JFrame implements ActionListener {

    JTextArea textArea;
    JFrame frame;
    Connection conn;
    Statement statement;
    String chosenDB;

    // Constructor
    public TextEditorScreen(Statement stm, Connection connection) {
        // Create a frame
        conn = connection;
        statement = stm;
        frame = new JFrame("JDBC Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Set metal look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            // Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {
        }

        // Text component
        textArea = new JTextArea();
        textArea.enableInputMethods(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 18));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                textArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create amenu for menu
        JMenu m1 = new JMenu("Query");
        JMenuItem execute = new JMenuItem("Execute");
        JMenuItem selectDB = new JMenuItem("Select DB");
        execute.addActionListener(this);
        selectDB.addActionListener(this);
        m1.add(execute);
        m1.add(selectDB);
        mb.add(m1);

        JMenu m2 = new JMenu("Edit");

        JMenuItem mi4 = new JMenuItem("cut");
        JMenuItem mi5 = new JMenuItem("copy");
        JMenuItem mi6 = new JMenuItem("paste");

        // Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);

        mb.add(m2);

        frame.setJMenuBar(mb);
        frame.add(textArea);
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    // If a button is pressed
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("cut")) {
            textArea.cut();
        } else if (s.equals("copy")) {
            textArea.copy();
        } else if (s.equals("paste")) {
            textArea.paste();
        }

        else if (s.equals("Execute")) {
            try {
                String fullQuery;

                if (chosenDB == null) {
                    fullQuery = textArea.getText();
                } else {
                    fullQuery = "USE " + chosenDB + ";" + textArea.getText();
                }
                JFrame table = new JFrame("Results");
                ResultSet rs = conn.createStatement().executeQuery(fullQuery);
                int colCount = rs.getMetaData().getColumnCount();
                String[] colNames = getColumnNames(rs, colCount);
                ArrayList<ArrayList<String>> rows = getRows(rs, colNames);
                String[][] data = convertData(rows);

                JTable jt = new JTable(data, colNames);
                jt.setBounds(1, 1, 1000, 3000);
                JScrollPane sp = new JScrollPane(jt);
                sp.setVisible(true);
                table.add(sp);
                table.setSize(1000, 3000);
                table.setVisible(true);
            } catch (SQLException err) {
                JOptionPane.showMessageDialog(null, "Something went wrong with your query error:\n" + err.toString(),
                        "JDBC",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (s.equals("Select DB")) {
            Object[] dbnames = getDBNames().toArray();
            chosenDB = (String) JOptionPane.showInputDialog(null,
                    "Select a Database to Query", "Select Database", JOptionPane.QUESTION_MESSAGE, null, dbnames,
                    dbnames[0]);
            frame.setTitle("JDBC Editor - Using " + chosenDB);
        }
    }

    public static ArrayList<ArrayList<String>> getRows(ResultSet rs, String[] colNames) {
        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

        try {
            while (rs.next()) {
                ArrayList<String> list = new ArrayList<String>();
                for (String col : colNames) {
                    list.add(rs.getString(col));
                }
                rows.add(list);
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rows;
    }

    public static String[][] convertData(ArrayList<ArrayList<String>> data) {
        int rowNum = data.size();
        int colNum = data.get(0).size();
        String[][] newdata = new String[rowNum][colNum];

        for (int i = 0; i < rowNum; i++) {
            String[] list = new String[colNum];
            for (int j = 0; j < colNum; j++) {
                list[j] = data.get(i).get(j);
            }
            newdata[i] = list;
        }

        return newdata;

    }

    public static String[] getColumnNames(ResultSet rs, int colCount) {
        String[] names = new String[colCount];
        try {
            for (int i = 0; i < colCount; i++) {
                names[i] = rs.getMetaData().getColumnName(i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public ArrayList<String> getDBNames() {
        ArrayList<String> dbnames = new ArrayList<String>();
        try {
            ResultSet names = statement.executeQuery("SELECT name FROM sys.databases;");

            while (names.next()) {
                String name = names.getString("name");
                dbnames.add(name);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return dbnames;
    }
}