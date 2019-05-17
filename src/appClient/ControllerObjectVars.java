package appClient;

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
    public TextField serverTextField;

    @FXML
    public TextArea receiveMsgBox;

    @FXML
    public TextArea msgBox;

    @FXML
    public Button connectServerBtn;

    @FXML
    public Button sendMsgBtn;

    @FXML
    public Label statusLabel;

    @FXML
    public Label chatStatusLabel;
}
