package Scheduler;


import Utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;


/**
 * The type Scheduler.
 */
public class Scheduler extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginScreen.fxml"));
        Controllers.LoginScreen loginScreen = new Controllers.LoginScreen();
        loader.setController(loginScreen);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    /**
     * Launches the application
     *
     * @param args standard language
     */
    public static void main(String[] args) {
        Connection conn = DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
