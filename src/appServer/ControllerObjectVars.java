package appServer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

class ControllerObjectVars
{
    @FXML
    public TextField portTextField;

    @FXML
    public TextArea receiveMsgBox;

    @FXML
    public TextArea msgBox;

    @FXML
    public Button startServerBtn;

    @FXML
    public Button sendMsgBtn;

    @FXML
    public Button clearMsgBtn;

    @FXML
    public Label statusLabel;

    @FXML
    public Label chatStatusLabel;
}
