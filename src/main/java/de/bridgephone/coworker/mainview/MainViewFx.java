
package de.bridgephone.coworker.mainview;

import de.bridgephone.coworker.xmlconfiguration.CheckResult;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.bridgephone.coworker.xmlconfiguration.XmlValueRecord.*;


public class MainViewFx {

    private static final String TAG="MainViewFx";

    Stage stage;

    private final Label[] label = new Label[3];
    private final TextField[] textField = new TextField[3];
    private final Button[] locateButton = new Button[3];
    private final String[] labelText = {
        "Scoring program",
        "Bridgephone.exe",
        "BWS directory"};
    private final String[] extensionText = {
        ".exe",
        ".exe",
        ".bws"};

    private final String ENG = "English";
    private final String FR = "Francais";
    private final String DE = "Deutsch";
    private final String NL = "Nederlands";
    private final String SLK = "Slovak";

//    private final boolean[] status = {TFNOK, TFNOK, TFNOK};
//    private static final boolean TFOK = true;
//    private static final boolean TFNOK = false;

    private GridPane gridPaneMain;
//    private int language;
    private MainViewControl control;
    private Locale currentLocale;
    private ResourceBundle resourceBundle;
    private Button submitButton;
    private final java.util.logging.Logger LOG= Logger.getLogger(TAG);
    public void setMainControl(MainViewControl control) {
        this.control = control;
    }

    public void setLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

//    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        resourceBundle = ResourceBundle.getBundle("App", currentLocale);
        String appName = resourceBundle.getString("Application.title");
        String versionString = resourceBundle.getString("Application.version");
        stage.setTitle(appName + " " + versionString);

        URL imageUrl=getClass().getResource("/icons/LogoBridgePhoneInvertPink16x16.png");
        Image logo2 = null;
        try {
            logo2 = new Image(new FileInputStream(imageUrl.getFile()));
        } catch (FileNotFoundException e) {
           LOG.log(Level.WARNING,"Could not read Logo");
        }
        stage.getIcons().add(logo2);


        // Create the registration form pane
        gridPaneMain = mainBridgeHarmonyPane();

        addUIControls(gridPaneMain);

        // Create a scene with the registration form gridPane as the root node.
        Scene scene = new Scene(gridPaneMain, 800, 500);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        primaryStage.show();
        LOG.info(TAG+"ready to show the scene");
    }

    private GridPane mainBridgeHarmonyPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.TOP_LEFT);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(10, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints
        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(130, 10, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnThreeConstrains = new ColumnConstraints(20, 20, Double.MAX_VALUE);
        columnThreeConstrains.setHalignment(HPos.LEFT);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

//    @SuppressWarnings("Convert2Lambda")
    private void addUIControls(GridPane gridPane) {

        final Menu menu1 = new Menu(resourceBundle.getString("Main.configuration"));
        final Menu menu2 = new Menu(resourceBundle.getString("Main.language"));
        final Menu menu3 = new Menu(resourceBundle.getString("Main.help"));

        MenuItem menu1Item1 = new MenuItem(resourceBundle.getString("Main.configuration"));
        MenuItem menu1Item2 = new MenuItem("Exit");
        menu1.getItems().addAll(menu1Item1, menu1Item2);
        menu1Item1.setOnAction(reconfigureEventHandler);
        menu1Item2.setOnAction(wxitEventHandler);
//        menuItem.setGraphic(new ImageView(new Image("flower.png")));

        MenuItem menu2Item1 = new MenuItem(ENG);
        menu2Item1.setOnAction(languageEventHandler);
        MenuItem menu2Item2 = new MenuItem(FR);
        menu2Item2.setOnAction(languageEventHandler);
        MenuItem menu2Item3 = new MenuItem(DE);
        menu2Item3.setOnAction(languageEventHandler);
        MenuItem menu2Item4 = new MenuItem(NL);
        menu2Item4.setOnAction(languageEventHandler);
        MenuItem menu2Item5 = new MenuItem(SLK);
        menu2Item5.setOnAction(languageEventHandler);
        menu2.getItems().addAll(menu2Item1, menu2Item2, menu2Item3, menu2Item4, menu2Item5);

        MenuItem menu3Item1 = new MenuItem(resourceBundle.getString("main.about"));
        menu3Item1.setOnAction(AboutEventHandler);
        MenuItem menu3Item2 = new MenuItem(resourceBundle.getString("Main.help"));
        menu3Item2.setOnAction(HelpEventHandler);
        menu3.getItems().addAll(menu3Item1, menu3Item2);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);
        gridPane.add(menuBar, 0, 0, 3, 1);
        GridPane.setHalignment(menuBar, HPos.LEFT);
//        GridPane.setMargin(menuBar, new Insets(0, 0,0,0));

        // Add Name Label
        labelText[0] = resourceBundle.getString("main.scoringprogram");
//        labelText[1]=resourceBundle.getString("Main.configuration");
        labelText[2] = resourceBundle.getString("main.bwsdirectory");


        prepareInputFields(SCORINGPROGRAM, label, labelText, textField, locateButton);

        prepareInputFields(PCBRIDGEPHONEPROGRAM, label, labelText, textField, locateButton);

        prepareInputFields(BWSDIR, label, labelText, textField, locateButton);

        // Add Submit Button
        submitButton = new Button(resourceBundle.getString("main.start"));
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));
        submitButton.setStyle("-fx-background-color: #f00;");
        submitButton.setOnAction(submitButtonEventHandler);
    }

    private void prepareInputFields(int fieldNo, Label[] label, String[] labelText,
                                    TextField[] textField1, Button[] locateButton) {

        // Add Name Label
        label[fieldNo] = new Label(labelText[fieldNo] + " : ");
        gridPaneMain.add(label[fieldNo], 0, fieldNo + 1);

        // Add Name Text Field
        textField[fieldNo] = new TextField();
        textField[fieldNo].setPrefHeight(40);
        gridPaneMain.add(textField[fieldNo], 1, fieldNo + 1);

        // Add Locate1 Button
        locateButton[fieldNo] = new Button("...");
        locateButton[fieldNo].setPrefHeight(40);
        locateButton[fieldNo].setDefaultButton(true);
        locateButton[fieldNo].setPrefWidth(40);
        gridPaneMain.add(locateButton[fieldNo], 2, fieldNo + 1, 1, 1);
        GridPane.setHalignment(locateButton[fieldNo], HPos.LEFT);
//        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        EventHandlerLocateButton<ActionEvent> locateButtonHandler = new EventHandlerLocateButton<>();
        locateButton[fieldNo].setOnAction(locateButtonHandler);
        locateButtonHandler.transferParm(fieldNo, textField1);
    }

//    @SuppressWarnings("Convert2Lambda")
    EventHandler<ActionEvent> languageEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            String s = ((MenuItem) e.getSource()).getText();
            LOG.log(Level.INFO,"Language event "+s);
            int i;

            switch (((MenuItem) e.getSource()).getText()) {

                case ENG: {
                    currentLocale = new Locale(ENG);
                    i = 0;
                    break;
                }
                case FR: {
                    currentLocale = new Locale(FR);
                    i = 1;
                    break;
                }
                case DE: {
                    currentLocale = new Locale(DE);
                    i = 2;
                    break;
                }
                case NL: {
                    currentLocale = new Locale(NL);
                    i = 3;
                    break;
                }
                case SLK: {
                    currentLocale = new Locale(SLK);
                    i = 4;
                    break;
                }
                default: {
                    currentLocale = new Locale(ENG);
                    i = 99;
                    LOG.log(Level.SEVERE,"WTF :Language out of range ");
                    break;
                }
            }
            control.changeLocale(currentLocale, i);

        }
    };


//    @SuppressWarnings("Convert2Lambda")
    EventHandler<ActionEvent> submitButtonEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Object object = e.getSource();
            LOG.info("Submit button " + object.toString());
             boolean b=   control.submitButtonPressed();
            enableDisableStart(b);

        }
    };


    @SuppressWarnings("Convert2Lambda")
    EventHandler<ActionEvent> reconfigureEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Object object = e.getSource();
            LOG.info("Reconfigure " + object.toString());
        }
    };

    @SuppressWarnings("Convert2Lambda")
    EventHandler<ActionEvent> wxitEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Object object = e.getSource();
            LOG.info("Exit " + object.toString());
        }
    };

    @SuppressWarnings("Convert2Lambda")
    EventHandler<ActionEvent> AboutEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Object object = e.getSource();
            LOG.info("About " + object.toString());
        }
    };

    @SuppressWarnings("Convert2Lambda")
    EventHandler<ActionEvent> HelpEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Object object = e.getSource();
            LOG.info("Help " + object.toString());
        }
    };

    void setTextField(int i, String s, boolean ok) {
        textField[i].setText(s);
        if (ok) {
            textField[i].setStyle("-fx-text-inner-color: blue;");
        } else {
            textField[i].setStyle("-fx-text-inner-color: red;");
        }

    }

    void enableDisableStart(boolean b) {
        if (!b) {
            submitButton.setStyle("-fx-background-color: #f00;");
        } else {
            submitButton.setStyle("-fx-background-color: #0f0;");
        }
    }

    /*
      This is a normal EventHandle.
      just fucking around with parmeters

     */
    private class EventHandlerLocateButton<ActionEvent extends Event> implements EventHandler<ActionEvent> {

        int fieldNo;
        TextField[] textField1;
        private boolean[] status;

        @Override
        public void handle(ActionEvent event) {
            String name = event.getEventType().getName();
            if (name.equals("ACTION")) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Search for " + labelText[fieldNo]);
                File f = fileChooser.showOpenDialog(stage);
                LOG.info("File chooser invoked for " + labelText[fieldNo]);
                if (f != null) {
                    CheckResult cr = control.newFileNameEntered(f, fieldNo);
                    if (cr.isResult()) {
                        try {
                            textField1[fieldNo].setText(cr.getFile().getCanonicalPath());
                            textField1[fieldNo].setStyle("-fx-text-inner-color: blue;");
                            LOG.info("Textfield  " + labelText[fieldNo] + " set to " + f.getCanonicalPath());

                        } catch (IOException e) {
                            textField1[fieldNo].setText("Exception" + e.getMessage());
                            textField1[fieldNo].setStyle("-fx-text-inner-color: red;");
                            LOG.log(Level.INFO, "Exception at  " + labelText[fieldNo] + "  " + e.getMessage());
                        }
                    } else {
                        textField1[fieldNo].setText(cr.getErrorMessage());
                        textField1[fieldNo].setStyle("-fx-text-inner-color: red;");
                        LOG.info(labelText[fieldNo] + cr.getErrorMessage());
                    }
                }
            }
        }

        public void transferParm(int fieldNo, TextField[] textField) {
            this.fieldNo = fieldNo;
            textField1 = textField;

        }

    }

}
