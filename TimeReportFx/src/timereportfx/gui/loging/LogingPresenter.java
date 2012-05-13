/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.gui.loging;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import timereportfx.TimeReportFx;
import timereportfx.models.entities.UtilisateurEntity;
import timereportfx.service.UtilisateurService;

/**
 *
 * @author Dimitri Lebel
 */
public class LogingPresenter implements Initializable {

    private UtilisateurService UJPA;
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
    private AnchorPane anchorPane;

    @FXML
    private void okClick(ActionEvent e) {
        UJPA = application.getTimeReportFxFactory().getUtilisateurService();
        UtilisateurEntity u = UJPA.findUtilisateurByNom(txt_userName.getText());
        if (u == null) {
            lbl_error.setOpacity(1);
        } else {
            application.getTimeReportFxFactory().getMainPresenter().logged(u);
        }

    }

    public void setApplication(TimeReportFx application) {
        this.application = application;
    }

    public Parent getView() {
        return (Parent) anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
