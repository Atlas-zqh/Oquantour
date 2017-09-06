package ui.util;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by island on 2017/3/12.
 */
public class ErrorBoxController {
    @FXML
    private Label errorLabel;

    public void setLabel(String text){
        errorLabel.setText(text);
    }
}
