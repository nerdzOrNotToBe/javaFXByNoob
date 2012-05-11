/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;

import timereportfx.gui.main.MainPresenter;
import com.sun.javafx.binding.BindingHelperObserver;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Dimitri Lebel
 */
public class InfoBulleController implements Initializable {

    @FXML
    private Button btnContinuer;
    @FXML
    private Button btnStop;
    @FXML
    private Label lblTimer;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label lbltacheEnCours;
    private Stage stage;
    private static final Integer STARTTIME = 15;
    private IntegerProperty timeSeconds;
    private Timeline timeline;
    private AnimationTimer timer;
    private MainPresenter main;

    public void setStage(Stage stage) {
        this.stage = stage;
        startTimerButton();
    }

    public void setMain(MainPresenter main) {
        this.main = main;
    }

    public void setLbltacheEnCoursText(String text) {
        this.lbltacheEnCours.setText(text) ;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        timeSeconds = new SimpleIntegerProperty(STARTTIME * 100);
        startTimerButton();
    }

    private void startTimerButton() {
        timeSeconds.set((STARTTIME + 1) * 100);
        timeline = new Timeline();
        timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                lblTimer.setText("( " + timeSeconds.divide(100).getValue().toString() + " )");
            }
        };
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (timeSeconds.get() < 100) {
                    timeline.stop();
                    timer.stop();
                    stage.close();
                }
            }
        };
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME + 1), onFinished, new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
        timer.start();

    }

    @FXML
    private void handleBtnContinuer(MouseEvent mouseEvent) {
        timeline.stop();
        timer.stop();
        stage.close();
    }

    @FXML
    private void handleBtnStoper(MouseEvent mouseEvent) {
        main.stopTache();
        timeline.stop();
        timer.stop();
        stage.close();
    }
}
