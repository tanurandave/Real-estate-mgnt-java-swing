/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a38;

/**
 *
 * @author randa
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url =
            "jdbc:mysql://localhost:3306/rentaldb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            String username = "root";
            String password = "root";

            con = DriverManager.getConnection(url, username, password);

            System.out.println("Connected");

        } catch (Exception e) {

            e.printStackTrace();
        }

        return con;
    }
}