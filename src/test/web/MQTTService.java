package test.web;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTService {
	String broker = "tcp://iot.eclipse.org:1883";
	String clientId = "JavaSample";
	MqttConnectOptions conOps;
	MqttClient client;
	MemoryPersistence persistence = new MemoryPersistence();

	public MQTTService(String clientId) {
		this.clientId = clientId;

	}

	public void begin(String server, int port, String username, String password) {
		broker = "tcp://" + server + ":" + port;
		try {
			client = new MqttClient(broker, clientId, persistence);

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conOps = new MqttConnectOptions();
		conOps.setCleanSession(true);
		conOps.setUserName(username);
		conOps.setPassword(password.toCharArray());

	}

	public void connect() throws MqttSecurityException, MqttException {
		client.connect(conOps);
	}

	public void publish(String topic, String message) throws MqttPersistenceException, MqttException {
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setPayload(message.getBytes());
		mqttMessage.setQos(0);
		mqttMessage.setRetained(false);
		client.publish(topic, mqttMessage);
	}

	public void subscribe(String topicFilter, MqttCallback callback) {
		try {
			client.subscribe(topicFilter);
			client.setCallback(callback);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return client.isConnected();
	}
}
