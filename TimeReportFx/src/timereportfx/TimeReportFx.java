/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx;


import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import timereportfx.controller.InfoBulleController;
import timereportfx.controller.LoggingController;
import timereportfx.controller.Main;
import timereportfx.models.entities.UtilisateurEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class TimeReportFx extends Application {

    private static EntityManagerFactory emf;
    private Stage primaryStage;
    private Stage stageLoggin;
    private UtilisateurEntity user;
    private LoggingController loggingController;
    private Main mainController;

    public static void main(String[] args) {
        Application.launch(TimeReportFx.class, args);
    }

    public static EntityManagerFactory getEMF() {
        return emf;
    }

    @Override
    public void start(Stage stage) throws Exception {
        emf = Persistence.createEntityManagerFactory("TimeReportFxPU");
        primaryStage = new Stage();
        mainController = (Main) OpenScene("view/TimeReport.fxml", primaryStage);
        mainController.setApplication(this);
        mainController.setStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setOpacity(0.95);
        primaryStage.show();
        logging();
    }

    public void logging() {
        try {
            stageLoggin = new Stage();
            loggingController = (LoggingController) OpenScene("view/Login.fxml", stageLoggin);
            loggingController.setApplication(this);
            stageLoggin.initModality(Modality.APPLICATION_MODAL);
            stageLoggin.show();
        } catch (IOException ex) {
            Logger.getLogger(TimeReportFx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logged(UtilisateurEntity u) {
        user = u;
        stageLoggin.close();
        mainController.findProjetTache();
        mainController.setUser(user);
    }

    private Initializable OpenScene(String fxml, Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = TimeReportFx.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(TimeReportFx.class.getResource(fxml));
        Parent page;
        try {
            page = (Parent) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

  
}
