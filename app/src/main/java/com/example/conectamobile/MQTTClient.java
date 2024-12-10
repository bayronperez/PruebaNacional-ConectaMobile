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
    // Método para conectar al servidor MQTT
    public void connect(String username, String password, IMqttActionListener callback) {
        try {
            // Crear opciones de conexión
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            // Intentar conectar al servidor MQTT usando las opciones configuradas
            client.connect(options, null, callback);
        } catch (MqttException e) {

            // Registrar el error si la conexión falla
            Log.e(TAG, "Error al intentar conectar: " + e.getMessage());
        }
    }

    // Método para suscribirse a un topic específico
    public void subscribe(String topic, int qos, IMqttActionListener callback) {
        try {
            client.subscribe(topic, qos, null, callback);
            Log.d(TAG, "Suscrito al topic: " + topic);
        } catch (MqttException e) {

            // Registrar el error si la suscripción falla
            Log.e(TAG, "Error al intentar suscribirse al topic: " + topic + ". " + e.getMessage());
        }
    }

    // Método para publicar un mensaje en un topic específico
    public void publish(String topic, String payload, int qos) {
        try {

            // Crear un mensaje MQTT con la carga útil especificada
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);

            // Publicar el mensaje en el topic especificado
            client.publish(topic, message);
            Log.d(TAG, "Mensaje publicado en el topic: " + topic + ", contenido: " + payload);
        } catch (MqttException e) {

            // Registrar el error si la publicación falla
            Log.e(TAG, "Error al intentar publicar en el topic: " + topic + ". " + e.getMessage());
        }
    }

    // Método para desconectar el cliente del servidor MQTT
    public void disconnect() {
        try {

            // Verificar si el cliente está conectado antes de intentar desconectarlo
            if (client.isConnected()) {
                client.disconnect();
                Log.d(TAG, "Cliente desconectado exitosamente");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error al intentar desconectar el cliente: " + e.getMessage());
        }
    }

    // Método para verificar si el cliente está conectado al servidor MQTT
    public boolean isConnected() {
        return client.isConnected();
    }
}


