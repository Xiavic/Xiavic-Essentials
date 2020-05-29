//package com.github.xiavic.essentials.Utils.Misc;
//import com.github.xiavic.essentials.Main;
//import org.bukkit.event.Listener;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class Databases implements Listener {
//    private Connection connection;
//    private String host, database, username, password;
//    private int port;
//    if(Main.database.getString("Database")=="MYSQL")
//
//    {
//        {
//            @Override
//            public void MySQLload () {
//            host = Main.database.getString("Host");
//            port = Main.database.getInt("Port");
//            database = Main.database.getString("Database");
//            username = Main.database.getString("Username");
//            password = Main.database.getString("Password");
//            try {
//                openConnection();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
//        }
//    }
//
//    public void openConnection() throws SQLException {
//        if (connection != null && !connection.isClosed()) {
//            return;
//        }
//    }
//}