package de.bridgephone.coworker;

import java.io.IOException;

import de.bridgephone.coworker.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}