/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

 package com.inno.view.startuppopup.main;

 import java.io.IOException;
 
 import javafx.application.Application;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Scene;
 import javafx.stage.Stage;
 import javafx.scene.Parent;

public class StartupPopupMainView extends Application {
	@Override
	public void start(Stage stage) throws IOException
	{

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/popup.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
		stage.setTitle("InnoEvent");
		stage.setResizable(false);
		stage.show();
		
	}

	public void run(String[] args) {
		Application.launch(args);
	}
}