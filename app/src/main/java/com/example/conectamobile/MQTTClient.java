package com.example.conectamobile;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTClient {

    private static final String TAG = "MQTTClient";

    private MqttAndroidClient client;
    private String serverUri = "ssl://broker.hivemq.com:8883";
    private String clientId;

    public MQTTClient(Context context, String clientId) {
        this.clientId = clientId;
        this.client = new MqttAndroidClient(context, serverUri, clientId);

        // Configurar el callback para manejar eventos de MQTT
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e(TAG, "Conexión perdida: " + (cause != null ? cause.getMessage() : "desconocida"));
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.d(TAG, "Mensaje recibido en el topic: " + topic + ", contenido: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Mensaje entregado con éxito");
            }
        });
    }

    public void connect(String username, String password, IMqttActionListener callback) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            client.connect(options, null, callback);
        } catch (MqttException e) {
            Log.e(TAG, "Error al intentar conectar: " + e.getMessage());
        }
    }

    public void subscribe(String topic, int qos, IMqttActionListener callback) {
        try {
            client.subscribe(topic, qos, null, callback);
            Log.d(TAG, "Suscrito al topic: " + topic);
        } catch (MqttException e) {
            Log.e(TAG, "Error al intentar suscribirse al topic: " + topic + ". " + e.getMessage());
        }
    }

    public void publish(String topic, String payload, int qos) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            Log.d(TAG, "Mensaje publicado en el topic: " + topic + ", contenido: " + payload);
        } catch (MqttException e) {
            Log.e(TAG, "Error al intentar publicar en el topic: " + topic + ". " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (client.isConnected()) {
                client.disconnect();
                Log.d(TAG, "Cliente desconectado exitosamente");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error al intentar desconectar el cliente: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }
}


