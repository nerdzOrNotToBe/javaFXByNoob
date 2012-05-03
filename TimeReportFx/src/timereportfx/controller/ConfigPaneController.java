/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;

import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import timereportfx.view.BouncingIcon;

/**
 *
 * @author Dimitri Lebel
 */
public class ConfigPaneController implements Initializable {

    @FXML
    private HBox taskBar;
    @FXML
    private AnchorPane pane;
    private Stage stage;
    private Scene currentScene;
    private Main main;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/Projet.png").toString(), "Configuration des projets", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                changeView("../view/ProjetPane.fxml");
            }
        }));
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/tasks.png").toString(), "Configuration des Taches", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }));
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/user.png").toString(), "Configuration des users", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
            }
        }));
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/group.png").toString(), "Configuration des groups", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }));
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/cube.png").toString(), "Configuration des catégories", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }));
        taskBar.getChildren().add(new BouncingIcon(getClass().getResource("../view/configure.png").toString(), "Configuration des préférences", new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }));

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void changeView(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane anchorPane = new AnchorPane();
        try {
            anchorPane = (AnchorPane) loader.load(in);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (pane.getChildren().isEmpty()) {
            pane.getChildren().add(anchorPane);
        } else if (!pane.getChildren().get(0).getId().equals(anchorPane.getId().toString())) {
            pane.getChildren().clear();
            pane.getChildren().add(anchorPane);
        }
    }
}
