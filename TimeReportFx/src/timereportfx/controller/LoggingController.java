/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import timereportfx.TimeReportFx;
import timereportfx.models.Utilisateur;

/**
 *
 * @author Dimitri Lebel
 */
public class LoggingController implements Initializable {

    private UtilisateurJpaController UJPA;
    private TimeReportFx application;
    @FXML
    private TextField txt_userName;
    @FXML
    private Button btn_ok;
    @FXML
    private Button btn_cancel;
    @FXML
    private Label lbl_error;

    @FXML
    private void okClick(ActionEvent e) {
        UJPA = new UtilisateurJpaController();
        Utilisateur u = UJPA.findUtilisateurByNom(txt_userName.getText());
        if (u == null ) {
            lbl_error.setOpacity(1);
        } else {
            application.logged(u);
        }

    }

    public void setApplication(TimeReportFx application) {
        this.application = application;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
