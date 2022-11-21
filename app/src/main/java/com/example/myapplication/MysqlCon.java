package com.example.myapplication;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
    // 資料庫定義
    String mysql_ip = "localhost";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "mqtt_Test";
//    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String url = "jdbc:mysql://192.168.56.1:3306/?user=superu";
//    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name+"?useCursorFetch=true";
//    String url = "jdbc:mysql://localhost:3306/mqtt_Test?useSSL=false&serverTimezone=CST";
//    String url = "url = jdbc:mysql://localhost:3306/mqtt_Test?serverTimezone=CST&characterEncoding=utf8&useUnicode=true&useSSL=false";

    String db_user = "superu";
    String db_password = "class100";

    public void run() {
        try {
            //目前有成功
//            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("JDBC_Driver", "Driver loaded!");
        } catch (ClassNotFoundException e) {
            Log.e("JDBC_Driver", "Driver loaded failed");
            return;
        }
//        try {
//            //新加的
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Log.v("Oracle_Driver","Driver loaded!");
//        }catch( ClassNotFoundException e) {
//            Log.e("Oracle_Driver","Driver loaded failed");
//            return;
//        }

        // 連接資料庫
        try {
            //失敗
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Log.v("DB", "MySQL connected!");
        } catch (SQLException e) {
            Log.e("DB", "MySQL connected failed.");
            Log.e("DB", e.toString());
        }
    }

    public String getData() {
        String data = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM test";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String id = rs.getString("id");
                String name = rs.getString("name");
                data += id + ", " + name + "\n";
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
