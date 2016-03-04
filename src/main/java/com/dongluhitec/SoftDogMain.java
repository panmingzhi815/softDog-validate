package com.dongluhitec;/**
 * Created by xiaopan on 2016-03-04.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.pan.decorate.StageDecorate;

import java.io.IOException;

public class SoftDogMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(SoftDogMain.class.getResource("importSn.fxml"));
        StageDecorate.decorate(primaryStage,root);
        primaryStage.show();
    }
}
