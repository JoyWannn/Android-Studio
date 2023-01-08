package com.example.myapplication;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import info.mqtt.android.service.MqttAndroidClient;

public class MysqlCon {
    // 資料庫定義
    String mysql_ip = "localhost";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "mqtt_Test";
    String url = "jdbc:mysql://us-cdbr-east-06.cleardb.net:3306";

    String db_user = "bda56e637d88e8";
    String db_password = "5da60d7e";
    Connection con;

    private PreparedStatement statement;
    private static final String SQL_INSERT = "INSERT INTO mqtt (quality_of_service,topic,message) VALUES (2,mqtt,forward)";

    public void run() {
        try {
            //加載驅動
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("JDBC_Driver", "Driver loaded!");
        } catch (ClassNotFoundException e) {
            Log.e("JDBC_Driver", "Driver loaded failed");
            return;
        }
        // 連接資料庫
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Log.v("DB", "MySQL connected!");
        } catch (SQLException e) {
            Log.e("DB", "MySQL connected failed.");
        }

    }

    public void insertData(String topic, MqttMessage message){
        try {
            //insert
            statement = con.prepareStatement(SQL_INSERT);
            statement.setInt(1, message.getQos());
            statement.setString(2, topic);
            statement.setBytes(3, message.getPayload());
            statement.executeUpdate();
            Log.v("DB", "insert success!");
        } catch (SQLException e) {
            Log.e("DB", "insert failed.");
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
