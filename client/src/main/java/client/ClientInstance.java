package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import client.gui.Controller;
import protocol.Fields;
import protocol.message_type_http.HttpBodyType;
import protocol.message_type_http.HttpHeader;
import protocol.message_type_http.HttpMessage;
import protocol.message_type_http.body.JsonBody;

public class ClientInstance {
	private String host;
	private int port;
	private HttpMessage request;
	private HttpMessage response;
	private String resource;
	private String bodyContent;
	private Controller controller;
	private String sessionId;
	
	public ClientInstance(String host, int port, Controller controller) {
		this.host = host;
		this.port = port;
		this.controller = controller;
		sessionId = " ";
	}
	
	public void runClient() {
		prepareRequest();
		sendAndReceive();
	}
	
	private void prepareRequest() {
		request = new HttpMessage(true);
		request.addHeader(new HttpHeader("Client", "My client"));
		request.addHeader(new HttpHeader(Fields.ContentType, HttpBodyType.JSON.getContentType()));
		request.addHeader(new HttpHeader(Fields.sessionId, sessionId));	
		request.setResource(resource);
		
		JsonBody body = new JsonBody();
		body.setValue(bodyBuilder().build());
		request.setBody(body);
		
		request.addHeader(new HttpHeader(Fields.ContentLength, String.valueOf(body.getLength())));
	}
	
	private void sendAndReceive() {
		controller.setRequest(request);
		response = sender(request);
		controller.setResponse(response);
		sessionId = response.getHeader(Fields.sessionId).getValue();
	}
	
	private HttpMessage sender(HttpMessage request) {
		try (	Socket socket = new Socket(host, port);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			) {
			writer.write(request.getFullMessage());
			writer.flush();
			return new HttpMessage(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			controller.setResponse("Klient: Brak po³¹czenia");
		}
		return null;
	}

	private JsonObjectBuilder bodyBuilder() {
		JsonObjectBuilder json = Json.createObjectBuilder();
		json.add("arg0", bodyContent);
		return json;
	}
	
	public HttpMessage getRequest() {
		return request;
	}

	public HttpMessage getResponse() {
		return response;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}
}
