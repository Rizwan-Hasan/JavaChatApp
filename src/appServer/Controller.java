package appServer;

import appServer.socketCode.SocketServer;
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
        this.receiveMsgBox.setEditable(false);
        this.receiveMsgBox.setWrapText(true);
        this.msgBox.setWrapText(true);
        this.statusLabel.setText("Server is off");
        this.chatStatusLabel.setText("Start server and connect with client to chat");
    }

    @FXML
    private void startServerBtnAction(ActionEvent event) {
        if (!this.connectionStatus) {
            try {
                // Create a Runnable
                Runnable task = () -> {
                    this.server = new SocketServer();
                    this.server.closeConnection();
                    int port = Integer.parseInt(this.portTextField.getText().trim());
                    this.server.setPort(port);
                    this.server.startServer();
                };
                // Run the task in a background thread
                this.backgroundThread = new Thread(task);
                // Start the thread
                this.backgroundThread.start();
                Thread.sleep(1000);

            } catch (Exception ignored) {
                this.statusLabel.setText("Wrong port number");
            }
            this.connectionStatus = true;
            System.out.println("Server started");
            this.statusLabel.setText("Server started");
            this.startServerBtn.setText("Stop Server");
            this.chatStatusLabel.setText("Waiting for client");
        } else {
            server.closeConnection();
            try {
                this.backgroundThread.interrupt();
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
            this.connectionStatus = false;
            System.out.println("Server closed");
            this.statusLabel.setText("Server closed");
            this.startServerBtn.setText("Start Server");
            this.chatStatusLabel.setText("Start server and connect with client to chat");
        }
        event.consume();
    }

    @FXML
    private void sendMsgBtnAction (ActionEvent event)
    {
        if (this.connectionStatus)
            this.server.sendMsg(this.msgBox.getText().trim());
        else
            this.chatStatusLabel.setText("Wrong try");

        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }

        event.consume();
    }

    @FXML
    private void clearMsgBtnAction (ActionEvent event)
    {
        this.receiveMsgBox.setText(null);
        this.msgBox.setText(null);
    }
}
