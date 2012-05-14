/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.gui.main;

import timereportfx.gui.config.ConfigPanePresenter;
import timereportfx.service.TacheJpaController;
import timereportfx.service.TimereportJpaController;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import timereportfx.TimeReportFx;
import timereportfx.gui.infoBulle.InfoBullePresenter;
import timereportfx.gui.loging.LogingPresenter;
import timereportfx.controller.ProjetController;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.ProjetEntity;
import timereportfx.models.entities.TacheEntity;
import timereportfx.models.entities.TimereportEntity;
import timereportfx.models.entities.UtilisateurEntity;
import timereportfx.service.ProjetService;

/**
 *
 * @author Dimitri Lebel
 */
public class MainPresenter implements Initializable {

    @FXML
    private Accordion accordion;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuItem menuConfig;
    private ToggleGroup tg;
    private ToggleButton bt;
    private TimeReportFx application;
    private ProjetService pjc;
    private TimereportJpaController timejc;
    private TacheJpaController tjc;
    private TacheEntity tache;
    private ProjetEntity projet;
    private UtilisateurEntity user;
    private TimereportEntity timereport;
    private Stage stagePoPup;
    private Timer timer;
    private int secondsTimer = 5;
    private Stage stage;
    private Stage configStage;
    private Stage stageLoggin;

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

    public Parent getView() {
        return (Parent) anchorPane;
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

    public void setProjetService(ProjetService pjc) {
        this.pjc = pjc;
    }

    public void setTimejc(TimereportJpaController timejc) {
        this.timejc = timejc;
    }

    public void setTjc(TacheJpaController tjc) {
        this.tjc = tjc;
    }

    public void findProjetTache() {
        List<ProjetEntity> projetList = pjc.findProjetEntities();
        tg = new ToggleGroup();
        Iterator iterator = projetList.iterator();
        while (iterator.hasNext()) {
            ProjetEntity projet1 = (ProjetEntity) iterator.next();
            FXMLLoader loader = new FXMLLoader();
            InputStream in = MainPresenter.class.getResourceAsStream("../../view/TitledPanePerso.fxml");
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(MainPresenter.class.getResource("../../view/TitledPanePerso.fxml"));
            TitledPane titledPane = new TitledPane();
            try {
                titledPane = (TitledPane) loader.load(in);
            } catch (IOException ex) {
                Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
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
        timejc = new TimereportJpaController();
        try {
            timejc.create(timereport);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void popPup() {

        stagePoPup = new Stage(StageStyle.UNDECORATED);
        InfoBullePresenter infoBullePresenter = application.getTimeReportFxFactory().getInfoBullePresenter();
        Scene scene = new Scene(infoBullePresenter.getView(), Color.TRANSPARENT);
        stagePoPup.setScene(scene);
        stagePoPup.sizeToScene();
        stagePoPup.initStyle(StageStyle.TRANSPARENT);
        stagePoPup.initOwner(null);
        stagePoPup.setX(javafx.stage.Screen.getPrimary().getBounds().getWidth() / 1.25);
        stagePoPup.setY(javafx.stage.Screen.getPrimary().getBounds().getHeight() / 1.25);
        infoBullePresenter.setStage(stagePoPup);
        infoBullePresenter.setMain(this);
        infoBullePresenter.setLbltacheEnCoursText(tache.getIdprojet().getNom() + " : " + tache.getNom());
        stagePoPup.show();
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

    public void onClickConfig(ActionEvent action) {
        configStage = new Stage();
        ConfigPanePresenter controller = application.getTimeReportFxFactory().getConfigPanePresenter();
        Scene scene = new Scene(controller.getView());
        controller.setApplication(application);
        configStage.setScene(scene);
        configStage.sizeToScene();
        controller.setStage(configStage);
        controller.setMain(this);
        configStage.show();
    }

    public void loging() {
        stageLoggin = new Stage();
        LogingPresenter logingPresenter = application.getTimeReportFxFactory().getLogingPresenter();
        logingPresenter.setApplication(application);
        Scene scene = new Scene(logingPresenter.getView());
        stageLoggin.setScene(scene);
        stageLoggin.initModality(Modality.APPLICATION_MODAL);
        stageLoggin.show();
    }

    public void logged(UtilisateurEntity u) {
        user = u;
        stageLoggin.close();
        findProjetTache();
        setUser(user);
    }
}
