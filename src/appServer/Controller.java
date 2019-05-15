package appServer;

import appServer.socketCode.SocketServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends ControllerObjectVars implements Initializable {
    private SocketServer server;
    private boolean connectionStatus = false;
    private Thread backgroundThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Nothing here
    }

    @FXML
    private void startServerBtnAction(ActionEvent event) {
        if (this.connectionStatus == false) {
            try {
                /*
                this.server = new SocketServer();
                this.server.closeConnection();
                int port = Integer.parseInt(this.portTextField.getText().trim());
                this.server.setPort(port);
                this.server.startServer();
                this.startServerBtn.setText("Stop Server");
                this.statusLabel.setText("Server started");
                this.connectionStatus = true;
                */

                // Create a Runnable
                Runnable task = () -> {
                    this.server = new SocketServer();
                    this.server.closeConnection();
                    int port = Integer.parseInt(this.portTextField.getText().trim());
                    this.server.setPort(port);
                    this.server.startServer();
                    Platform.runLater(() -> this.startServerBtn.setText("Stop Server"));
                    Platform.runLater(() -> this.statusLabel.setText("Server started"));
                    this.connectionStatus = true;
                };
                // Run the task in a background thread
                this.backgroundThread = new Thread(task);
                // Start the thread
                this.backgroundThread.start();

            } catch (Exception ignored) {
                this.statusLabel.setText("Wrong port number");
            }
            System.out.println("Server started");
        } else {
            server.closeConnection();
            try {
                this.backgroundThread.interrupt();
            } catch (Exception ignored) {}
            this.connectionStatus = false;
            this.statusLabel.setText("Server closed");
            this.startServerBtn.setText("Start Server");
            System.out.println("Server closed");
        }
        event.consume();
    }
}
