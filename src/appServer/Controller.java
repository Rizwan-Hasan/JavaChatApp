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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Nothing here
    }

    @FXML
    private void startServerBtnAction(ActionEvent event) {
        if (this.connectionStatus == false) {
            try {
                this.server = new SocketServer();
                this.server.closeConnection();
                int port = Integer.parseInt(this.portTextField.getText().trim());
                this.server.setPort(port);
                this.server.startServer();
                this.startServerBtn.setText("Stop Server");
                this.statusLabel.setText("Server started");
                this.connectionStatus = true;
            } catch (Exception e) {
                this.statusLabel.setText("Wrong port number");
            }
        } else {
            server.closeConnection();
            this.statusLabel.setText("Server closed");
            this.startServerBtn.setText("Start Server");
        }
        event.consume();
    }


}
