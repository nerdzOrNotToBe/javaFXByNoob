/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;

import timereportfx.service.TacheJpaController;
import timereportfx.service.TimereportJpaController;
import timereportfx.service.ProjetJpaController;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import timereportfx.TimeReportFx;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.ProjetEntity;
import timereportfx.models.entities.TacheEntity;
import timereportfx.models.entities.TimereportEntity;
import timereportfx.models.entities.UtilisateurEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class Main implements Initializable {

    @FXML
    private Accordion accordion;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuItem menuConfig;
    private ToggleGroup tg ;
    private ToggleButton bt;
    private TimeReportFx application;
    private ProjetEntity projet;
    private ProjetJpaController pjc;
    private TacheEntity tache;
    private TacheJpaController tjc;
    private UtilisateurEntity user;
    private TimereportEntity timereport;
    private Stage stagePoPup;
    private Timer timer;
    private int secondsTimer = 5;
    private Stage stage;
    private Stage configStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public ToggleButton getBt() {
        return bt;
    }

    public void setBt(ToggleButton bt) {
        this.bt = bt;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                if (timer != null) {
                    timer.cancel();
                }
                if (stagePoPup != null) {
                    stagePoPup.close();
                }
                Platform.exit();
            }
        });

    }

    public Stage getStage() {
        return stage;
    }

    public int getSecondsTimer() {
        return secondsTimer;
    }

    public void setSecondsTimer(int secondsTimer) {
        this.secondsTimer = secondsTimer;
    }

    public void setApplication(TimeReportFx application) {
        this.application = application;
    }

    public void setUser(UtilisateurEntity user) {
        this.user = user;
    }

    public ProjetEntity getProjet() {
        return projet;
    }

    public void setProjet(ProjetEntity projet) {
        this.projet = projet;
    }

    public void findProjetTache() {
        pjc = new ProjetJpaController();
        List<ProjetEntity> projetList = pjc.findProjetEntities();
        tg = new ToggleGroup();
        Iterator iterator = projetList.iterator();
        while (iterator.hasNext()) {
            ProjetEntity projet1 = (ProjetEntity) iterator.next();
            FXMLLoader loader = new FXMLLoader();
            InputStream in = Main.class.getResourceAsStream("../view/TitledPanePerso.fxml");
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(Main.class.getResource("../view/TitledPanePerso.fxml"));
            TitledPane titledPane = new TitledPane();
            try {
                titledPane = (TitledPane) loader.load(in);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ProjetController projetController = (ProjetController) loader.getController();
            projetController.setProjet(projet1);
            projetController.setMain(this);
            titledPane.setText(projet1.getNom());
            projetController.setBtnWidth(accordion.getPrefWidth());
            projetController.setTg(tg);
            projetController.AfficheTache();

            accordion.getPanes().add(titledPane);

        }
    }

    public void startTache(TacheEntity get) {
        stage.setIconified(true);
        timer = new Timer(false);
        timer.schedule(new PopPupTask(), secondsTimer * 1000, secondsTimer * 1000);
        tache = get;
        timereport = new TimereportEntity();
        timereport.setIdtache(tache);
        timereport.setTsDebut(new Date());
        timereport.setIdutilisateur(user);

    }

    public void stopTache() {
        if (timer != null) {
            timer.cancel();
        }
        if (timereport == null) {
            return;
        }
        bt.setSelected(false);
        stage.setIconified(false);
        timereport.setTsFin(new Date());
        timereport.setDuree((new Long(timereport.getTsFin().getTime()).intValue() - new Long(timereport.getTsDebut().getTime()).intValue()) / (60 * 1000));
        TimereportJpaController timejc = new TimereportJpaController();
        try {
            timejc.create(timereport);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void popPup() {
        try {
            stagePoPup = new Stage(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            InputStream in = TimeReportFx.class.getResourceAsStream("view/InfoBulle.fxml");
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(TimeReportFx.class.getResource("view/InfoBulle.fxml"));
            Parent page;
            try {
                page = (Parent) loader.load(in);
            } finally {
                in.close();
            }
            InfoBulleController controller = (InfoBulleController) loader.getController();
            Scene scene = new Scene(page, Color.TRANSPARENT);
            stagePoPup.setScene(scene);
            stagePoPup.sizeToScene();
            stagePoPup.initStyle(StageStyle.TRANSPARENT);
            stagePoPup.initOwner(null);
            stagePoPup.setX(javafx.stage.Screen.getPrimary().getBounds().getWidth() / 1.25);
            stagePoPup.setY(javafx.stage.Screen.getPrimary().getBounds().getHeight() / 1.25);
            controller.setStage(stagePoPup);
            controller.setMain(this);
            controller.setLbltacheEnCoursText(tache.getIdprojet().getNom() + " : " + tache.getNom());
            stagePoPup.show();
        } catch (IOException ex) {
            Logger.getLogger(TimeReportFx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class PopPupTask extends TimerTask {

        @Override
        public void run() {
            Platform.runLater((new Runnable() {

                @Override
                public void run() {
                    popPup();
                }
            }));
        }
    }
    public void onClickConfig(ActionEvent action){
            try {
            configStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            InputStream in = TimeReportFx.class.getResourceAsStream("view/ConfigPane.fxml");
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(TimeReportFx.class.getResource("view/ConfigPane.fxml"));
            Parent page;
            try {
                page = (Parent) loader.load(in);
            } finally {
                in.close();
            }
            ConfigPaneController controller = (ConfigPaneController) loader.getController();
            Scene scene = new Scene(page);
            configStage.setScene(scene);
            configStage.sizeToScene();
            controller.setStage(configStage);
            controller.setMain(this);
            configStage.show();
        } catch (IOException ex) {
            Logger.getLogger(TimeReportFx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
