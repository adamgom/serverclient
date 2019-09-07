package client.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import client.ClientInstance;

public class Gui extends Application {
	Stage primaryStage;
	ClientInstance client;
	Controller controller;
	
	public Gui(Stage primaryStage) {
		controller = new Controller();
		client = new ClientInstance("localhost", 8080, controller);
		controller.setClient(client);
		this.primaryStage = primaryStage;
		start(this.primaryStage);
	}
	
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Gui.fxml"));
		loader.setController(controller);
		StackPane stackPane = null;

		try {
			stackPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(stackPane, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
