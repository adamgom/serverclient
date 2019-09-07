package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import protocol.message_type_http.HttpMessage;
import client.ClientInstance;

public class Controller {
	@FXML TextField resource, bodyContent;
	@FXML Button send;
	@FXML TextArea request;
	@FXML TextArea response;
	ClientInstance client;
	
	public Controller() {
		client = null;
	}
	
	public void initialize() {
		resource.setText("helloworld");
		request.setText("Czekam na zapytanie");
		response.setText("Czekam na odpowiedz serwera");
		bodyContent.setText("Adam");
		send.addEventFilter(ActionEvent.ACTION, e -> sendButtonAction());
	}
	
	private void sendButtonAction() {
		client.setResource(resource.getText());
		client.setBodyContent(bodyContent.getText());
		client.runClient();
	}

	public void setRequest(HttpMessage request) {
		this.request.setText(request.getFullMessage());
	}

	public void setResponse(HttpMessage response) {
		this.response.setText(response.getFullMessage());
	}
	
	public void setResponse(String response) {
		this.response.setText(response);
	}
	
	public void setClient(ClientInstance client) {
		this.client = client;
	}
}
