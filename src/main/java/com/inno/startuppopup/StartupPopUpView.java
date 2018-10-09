/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 4th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

 package com.inno.startuppopup;

 import java.io.IOException;
 
 import javafx.application.Application;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Scene;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;

public class StartupPopUpView extends Application {
	@Override
	public void start(Stage stage) throws IOException
	{
	    // Create the FXMLLoader 
		FXMLLoader loader = new FXMLLoader();
		
		// Path to the FXML File

		loader.setLocation(getClass().getResource("/fxml/popUp.fxml"));
		
		// Create the Pane and all Details
		VBox root = (VBox) loader.load();
		
		StartupPopUpViewController controller = loader.getController();
		// Create the Scene
		Scene scene = new Scene(root);
		// Set the Scene to the Stage
		stage.setScene(scene);
		// Set the Title to the Stage
		stage.setTitle("InnoEvent");
		stage.setResizable(false);
		// Display the Stage
		
		controller.init(stage);
		
		stage.show();
	}

	public void run(String[] args) {
		Application.launch(args);
	}
}
