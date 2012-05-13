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
import timereportfx.gui.infoBulle.InfoBullePresenter;
import timereportfx.gui.loging.LogingPresenter;
import timereportfx.gui.main.MainPresenter;
import timereportfx.models.entities.UtilisateurEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class TimeReportFx extends Application {

    private static EntityManagerFactory emf;
    private Stage primaryStage;
    
    private UtilisateurEntity user;
    private TimeReportFxFactory timeReportFxFactory;

    public TimeReportFxFactory getTimeReportFxFactory() {
        return timeReportFxFactory;
    }

    public static void main(String[] args) {
        Application.launch(TimeReportFx.class, args);
    }

    public static EntityManagerFactory getEMF() {
        return emf;
    }

    @Override
    public void start(Stage stage) throws Exception {
     //   emf = Persistence.createEntityManagerFactory("TimeReportFxPU");
        emf = Persistence.createEntityManagerFactory("TimeReportFxPU2");
        timeReportFxFactory = new TimeReportFxFactory();
        
        primaryStage = new Stage();
        MainPresenter mainPresenter = timeReportFxFactory.getMainPresenter();       
        mainPresenter.setApplication(this);
        Scene scene = new Scene(mainPresenter.getView());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        mainPresenter.setStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setOpacity(0.95);
        primaryStage.show();
        mainPresenter.loging();
    }


  
}
