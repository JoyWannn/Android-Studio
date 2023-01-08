package com.example.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

//import org.eclipse.paho.android.service.MqttAndroidClient;
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

public class MainActivity extends AppCompatActivity{

    private final String MQTTHOST = "tcp://broker.hivemq.com:1883";

    private final String TAG = "button_check";

    MqttFunction mqttFunction = new MqttFunction();

    public static MqttAndroidClient client;

    int flag = 0;

    public static VideoView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this, MQTTHOST, clientId, Ack.AUTO_ACK);

        Button connectButton = findViewById(R.id.StartButton);
        Button gameButton = findViewById(R.id.GameButton);
        Button displayButton = findViewById(R.id.DisplayButton);

        connectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick1");
                mqttFunction.connect();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MysqlCon con = new MysqlCon();
                        con.run();
                    }
                }).start();
                gameButton.setVisibility(View.VISIBLE);
                displayButton.setVisibility(View.VISIBLE);
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, ChooseActivity.class);
//                startActivity(intent);
            }
        });

        gameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick2");
                mqttFunction.publish("play");
                View ctrLayout=findViewById(R.id.controller_Layout);
                ctrLayout.setVisibility(View.VISIBLE);
                View btnLayout = findViewById(R.id.buttonLayout);
                btnLayout.setVisibility(View.INVISIBLE);
            }
        });
        displayButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick3");
                mqttFunction.subscribe();
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.e(TAG,"connect lost!");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d(TAG,"message arrived:"+message);
                        String mes = message.toString();
                        if(mes.equals("play")){
                            videoPlay();
                            Log.d(TAG,"play video");
                        }
                        else if(mes.equals("pause")){
                            display.pause();
                        }
                        else if(mes.equals("start")){
                            display.start();
                        }
                    }
                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            }
        });
        //button of controller layout
        Button leftButton = findViewById(R.id.button_left);
        Button forwardButton = findViewById(R.id.button_forward);
        Button rightButton = findViewById(R.id.button_right);
        Button pauseButton = findViewById(R.id.button_pause);
        leftButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClickLeft");
                mqttFunction.publish("left");
            }
        });
        rightButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClickLeft");
                mqttFunction.publish("right");
            }
        });
        forwardButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClickLeft");
                mqttFunction.publish("forward");
            }
        });

        pauseButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClickPause");
                if(flag == 0){
                    mqttFunction.publish("pause");
                    flag = 1;
                }
                else if(flag==1){
                    mqttFunction.publish("start");
                    flag = 0;
                }

            }
        });
    }
    public void videoPlay(){
        display = findViewById(R.id.videoDisplay);
        display.setVisibility(View.VISIBLE);
        View btnLayout = findViewById(R.id.buttonLayout);
        btnLayout.setVisibility(View.INVISIBLE);
        Uri uri = Uri.parse("android.resource://com.example.myapplication/"+R.raw.gallery);
        display.setVideoURI(uri);
        display.start();
    }
}