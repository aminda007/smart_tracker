package com.example.aminda.smart_tracker;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by uwin5 on 06/03/18.
 */


public class MqttClientMy {

    private MqttAndroidClient client;
    private String TAG = "MqttClient";
    private boolean connected = false;

    public MqttClientMy(Context context , String clientId , MqttCallback callback){

        client = new MqttAndroidClient(context, "tcp://167.99.195.237:1883", clientId);
        client.setCallback(callback);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    setConnected(true);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure",exception);
                    setConnected(false);
                }


            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void disconnect(){
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeToTopic(String topic){

        try {
            client.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exceptionst subscribing");
            ex.printStackTrace();
        }

    }

    public void publishToTopic(String topic , String message){

        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = message.getBytes("UTF-8");
            MqttMessage mqttMessage = new MqttMessage(encodedPayload);
            client.publish(topic, mqttMessage);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }

    }


    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
