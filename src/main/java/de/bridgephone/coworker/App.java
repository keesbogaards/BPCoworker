package de.bridgephone.coworker;


import de.bridgephone.coworker.bplogger.BPLoggerOldStyle;
import de.bridgephone.coworker.mainview.MainViewControl;
import de.bridgephone.coworker.mainview.MainViewFx;
import de.bridgephone.coworker.xmlconfiguration.XmlHarmonyConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;





/**
 * JavaFX App
 */
public class App extends Application {
    private static final String TAG="App";

    private static Scene scene;
    private XmlHarmonyConfiguration xmlConfiguration;
    private final boolean test;
    private final Logger LOG=Logger.getLogger(TAG);

    public App() {
        test = false;
    }


    @Override
    public void start(Stage stage) throws IOException {
        //Attention: first define workdir, next BPLogger, next start logging
    // for testing purposes manually set "test' to true
    xmlConfiguration=new XmlHarmonyConfiguration(test);
    String workDirUrl=    xmlConfiguration.defineWorkdir();
    BPLoggerOldStyle logger= BPLoggerOldStyle.getInstance();
    logger.init(workDirUrl, Level.CONFIG);
    if (!xmlConfiguration.initialize(workDirUrl)){
        //TODO send user a message
    }
    LOG.info("Coworker start logging");
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/primary.fxml"));
    Parent root=loader.load();
    scene = new Scene(root, 640, 480);
    stage.setScene(scene);


//    String s = xmlConfiguration.getStatus();
//    if (!s.isEmpty()) {
////               JOptionPane.showMessageDialog(, s);
//        return;
//    }
    xmlConfiguration.checkOutXmlRecord();
    MainViewControl mainControl= new MainViewControl();
    MainViewFx mainViewFx= new MainViewFx();
    mainViewFx.setMainControl(mainControl);
    mainViewFx.setLocale(xmlConfiguration.getCurrentLocale());
    mainControl.setViewFx(mainViewFx);
    mainViewFx.start(stage);
    mainControl.initialFillView(xmlConfiguration);
    }


    static void setRoot(String fxml) throws IOException {
        Parent parent=loadFXML(fxml);
        scene.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }


}