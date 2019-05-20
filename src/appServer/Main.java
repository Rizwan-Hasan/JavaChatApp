package appServer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        primaryStage.setTitle("Java Chat Application (Server)");
        primaryStage.getIcons().add(new Resources().icon);
        Scene scene = new Scene(root);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("resources/stylesheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
