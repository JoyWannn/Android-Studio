package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public class MainActivity extends AppCompatActivity {
    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String USERNAME = "JoyWan";
    static String PASSWORD = "jojojojo123";

    String topicName = "MQTT Test";
    static String TAG = "JoyWan";
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        final TextView text_view = (TextView) findViewById(R.id.text_view);



            String clientId = MqttClient.generateClientId();
//        String clientId = "mqttx_5d1c1306";
            //client端設定
            //client = new MqttAndroidClient(this, MQTTHOST, clientId, MqttAndroidClient.Ack.AUTO_ACK);
        client = new MqttAndroidClient(this, MQTTHOST, clientId, Ack.AUTO_ACK);

        Button connectButton = findViewById(R.id.button1);
        connectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick1");
                MqttConnectOptions options = new MqttConnectOptions();

                //新增
                options.setCleanSession(true);
                options.setAutomaticReconnect(true);

                options.setUserName(USERNAME);
                options.setPassword(PASSWORD.toCharArray());
                IMqttToken token = null;
                token = client.connect();
                if (client.isConnected() == false) {
                    Log.d(TAG, "client.isConnected() == false");
                }
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Log.d(TAG, "onSuccess");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems
                        Log.d(TAG, "onFailure");
                    }
                });
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        MysqlCon con = new MysqlCon();
                        con.run();
                    }
                }).start();
            }
        });

        Button publishButton = findViewById(R.id.button2);
        publishButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish("Forward");
                Log.d(TAG, "onClick2");
            }
        });
          Button subscribeButton = findViewById(R.id.button3);
                subscribeButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        subscribe();
                        Log.d(TAG, "onClick3");
                    }
        });
    }
    public void publish(String payload) {
        String topic = topicName;
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        message.setQos(0);

//        client.publish(topic, message);
        client.publish(topic, message,null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, "publish succeed!");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i(TAG, "publish failed!");
            }
        });
    }
    public void subscribe(){
        String topic = topicName;
        int qos = 1;
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // The message was published
                Log.i(TAG, "subscribe succeed!");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Log.i(TAG, "subscribe failed!");
            }
        });
    }
//
//    private static final String SQL_INSERT = "INSERT INTO `Messages` (`message`,`topic`,`quality_of_service`) VALUES (?,?,?)";
//    private PreparedStatement statement;
//    private mySQL_URL = ""
//    public SubscribeCallback(final MqttClient mqttClient) {
//        this.mqttClient = mqttClient;
//
//        try {
//            final Connection conn = DriverManager.getConnection(JDBC_URL);
//            statement = conn.prepareStatement(SQL_INSERT);
//        } catch (SQLException e) {
//            log.error("Could not connect to database. Exiting", e);
//            System.exit(1);
//        }
//    }
//    @Override
//    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
//
//        //Let's assume we have a prepared statement with the SQL.
//        try {
//            statement.setBytes(1, message.getPayload());
//            statement.setString(2, topic.getName());
//            statement.setInt(3, message.getQos());
//
//            //Ok, let's persist to the database
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            log.error("Error while inserting", e);
//        }
//
//    }
}