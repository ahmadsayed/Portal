package test.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	MQTTService getMqttService(String clientId, String host, String port, String username, String password) throws MqttSecurityException, MqttException {
		MQTTService mqttService = new MQTTService(clientId);
		mqttService.begin(host, Integer.valueOf(port), username, password);
		mqttService.connect();
		return mqttService;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("command");
		if (command.equalsIgnoreCase("connect")) {
			String clientId = request.getParameter("clientId");
			String host = request.getParameter("host");
			String port = request.getParameter("port");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			try {
				request.getSession().putValue("mqttService", getMqttService(clientId, host, port, username, password));
				response.getOutputStream().println("Success");
			} catch (MqttSecurityException e) {
				response.getOutputStream().println("Security Exception : " + e.getMessage());
			} catch (MqttException e) {
				response.getOutputStream().println("Unable to Connect : " + e.getMessage());
			} catch (Exception e) {
				response.getOutputStream().println("Technical error");
			}

		} else if (command.equalsIgnoreCase("publish")) {
			String topic = request.getParameter("topic");
			String payload = request.getParameter("payload");
			try {
				if (request.getSession().getValue("mqttService") != null) {
					((MQTTService) request.getSession().getValue("mqttService")).publish(topic, payload);
					response.getOutputStream().println("published sucessfully");
				} else {
					response.getOutputStream().println("Broker not connected");
				}
			} catch (Exception ex) {
				response.getOutputStream().println("Failed to publish");
			}
		} else if (command.equalsIgnoreCase("subscribe")) {
			String topic = request.getParameter("topic");
			
		}
	}

}
