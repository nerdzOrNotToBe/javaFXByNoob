/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.gui.config;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import timereportfx.gui.main.MainPresenter;
import timereportfx.gui.projet.ProjetPanePrensenter;
import timereportfx.view.BouncingIcon;

/**
 *
 * @author Dimitri Lebel
 */
public class ConfigPanePresenter implements Initializable {

    @FXML
    private HBox taskBar;
    @FXML
    private AnchorPane pane;
    private Stage stage;
    private Scene currentScene;
    private MainPresenter main;
    
    private ProjetPanePrensenter projetPanePrensenter;
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../../view/Projet.png").toString(), "Configuration des projets", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                changeView(projetPanePrensenter.getView());
            }
        }));
//        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/tasks.png").toString(), "Configuration des Taches", new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent arg0) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        }));
//        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/user.png").toString(), "Configuration des users", new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent arg0) {
//            }
//        }));
//        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/group.png").toString(), "Configuration des groups", new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent arg0) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        }));
//        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/cube.png").toString(), "Configuration des catégories", new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent arg0) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        }));
//        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/configure.png").toString(), "Configuration des préférences", new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent arg0) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        }));

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public MainPresenter getMain() {
        return main;
    }

    public void setMain(MainPresenter main) {
        this.main = main;
    }

    public void changeView(Parent pane) {
   
            this.pane.getChildren().add(pane);
    }
}
