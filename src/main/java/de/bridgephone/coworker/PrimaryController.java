package de.bridgephone.coworker;

import java.io.IOException;

import de.bridgephone.coworker.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
