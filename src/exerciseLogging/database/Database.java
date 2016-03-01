package exerciseLogging.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Database() {}

    private static Connection conn;

    public static void connect() {
        try {
            File f = new File("database");
            System.out.println("Database already exists: " + f.exists());
            if (!f.exists()) {
                conn = DriverManager.getConnection("jdbc:derby:database;create=true");
            } else {
                conn = DriverManager.getConnection("jdbc:derby:database;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        File dbscript = new File("resources/database/dbscript.txt");
        System.out.println("Found script to create tables: " + dbscript.exists());
    }

    public static void main(String[] args) {
        connect();
        createTables();
        disconnect();
    }

}
