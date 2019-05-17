package appClient;

import appClient.socketCode.SocketClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends ControllerObjectVars implements Initializable {
    private SocketClient client;
    private boolean connectionStatus = false;
    private Thread backgroundThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.receiveMsgBox.setEditable(false);
        this.statusLabel.setText("← Let's start");
        this.chatStatusLabel.setText("Start server, then connect to start chatting");
    }

    @FXML
    private void connectServerBtnAction(ActionEvent event) {
        if (!this.connectionStatus) {
            try {
                // Create a Runnable
                Runnable task = () -> {
                    this.client = new SocketClient();
                    this.client.closeConnection();
                    int port = Integer.parseInt(this.portTextField.getText().trim());
                    String serverIp = this.serverTextField.getText().trim();
                    this.client.setPort(port);
                    this.client.setServer(serverIp);
                    this.client.connectServer();
                };
                // Run the task in a background thread
                this.backgroundThread = new Thread(task);
                // Start the thread
                this.backgroundThread.start();
                Thread.sleep(1000);

            } catch (Exception ignored) {
                this.statusLabel.setText("Wrong server or port number");
            }
            this.connectionStatus = true;
        } else {
            client.closeConnection();
            try {
                this.backgroundThread.interrupt();
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
            this.connectionStatus = false;
            System.out.println("Server closed");
            this.statusLabel.setText("Server closed");
            this.connectServerBtn.setText("Connect");
            this.chatStatusLabel.setText("Start server and connect with to chat");
        }
        event.consume();
    }

    @FXML
    private void sendMsgBtnAction (ActionEvent event)
    {
        if (this.connectionStatus)
            this.client.sendMsg(this.msgBox.getText().trim());
        else
            this.chatStatusLabel.setText("Wrong try");
        event.consume();
    }
}

