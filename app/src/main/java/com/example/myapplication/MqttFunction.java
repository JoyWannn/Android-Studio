package com.example.myapplication;

import static com.example.myapplication.MainActivity.client;
import static com.example.myapplication.MainActivity.display;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public class MqttFunction extends AppCompatActivity{
    private final String USERNAME = "JoyWan";
    private final String PASSWORD = "jojojojo123";

    private final String topicName = "MQTT Test";
    private final String TAG = "MQTT";

    public void connect(){
        MqttConnectOptions options = new MqttConnectOptions();

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
    }
    public void publish(String payload) {
        String topic = topicName;
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        message.setQos(0);

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
        int qos = 0;
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
                // The subscription could not be performed
                Log.i(TAG, "subscribe failed!");
            }
        });
//        client.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//                Log.e(TAG,"connect lost!");
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                Log.d(TAG,"message arrived:"+message);
//                String mes = message.toString();
//                if(mes.equals("play")){
//                    videoPlay();
//                    Log.d(TAG,"play video");
//                }
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });
    }
//    public void videoPlay(){
//        display = findViewById(R.id.videoDisplay);
//        display.setVisibility(View.VISIBLE);
//        View btnLayout = findViewById(R.id.buttonLayout);
//        btnLayout.setVisibility(View.INVISIBLE);
//        Uri uri = Uri.parse("android.resource://com.example.myapplication/"+R.raw.gallery);
//        display.setVideoURI(uri);
//        display.start();
//    }

}
