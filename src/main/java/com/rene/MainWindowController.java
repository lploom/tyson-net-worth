package com.rene;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.Executors;

public class MainWindowController {

    public Label netWonLbl;
    public BorderPane rootPane;

    private Stage primaryStage;
    private boolean resizing;
    private double yDragOffset;
    private double xDragOffset;
    private double xResizeOffset;
    private double yResizeOffset;

    PokerTrackerDAO pokerTrackerDAO = new PokerTrackerDAO();

    public void setPrimaryStage(Stage stage) {
        pokerTrackerDAO.init(new Conf());
        makeMoveable(stage);
        stage.initStyle(StageStyle.UNDECORATED);
        this.primaryStage = stage;
    }

    private void makeMoveable(Stage stage) {
        rootPane.setOnMousePressed(event -> {
            if (event.getX() > stage.getWidth() - 10
                    && event.getX() < stage.getWidth() + 10
                    && event.getY() > stage.getHeight() - 10
                    && event.getY() < stage.getHeight() + 10) {
                resizing = true;
                xResizeOffset = stage.getWidth() - event.getX();
                yResizeOffset = stage.getHeight() - event.getY();
            } else {
                resizing = false;
                xDragOffset = event.getSceneX();
                yDragOffset = event.getSceneY();
            }
        });
        rootPane.setOnMouseDragged(event -> {
            if (resizing) {
                stage.setWidth(event.getX() + xResizeOffset);
                stage.setHeight(event.getY() + yResizeOffset);
            } else {
                stage.setX(event.getScreenX() - xDragOffset);
                stage.setY(event.getScreenY() - yDragOffset);
            }
        });
    }

    public void startUpdateProcess() {
        Executors.newSingleThreadExecutor().submit(new Updator());
    }

    public void updateNewWonLabel(double netWon) {
        Platform.runLater(() -> {
            netWonLbl.setText(String.valueOf(netWon));
        });

    }

    private class Updator implements Runnable {
        @Override
        public void run() {
            while (true) {
                double netWon = pokerTrackerDAO.querySessionNetWon();
                updateNewWonLabel(netWon);
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

}