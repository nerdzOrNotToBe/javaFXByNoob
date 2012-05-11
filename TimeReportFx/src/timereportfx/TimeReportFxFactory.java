/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import timereportfx.gui.main.MainPresenter;
import timereportfx.gui.projet.ProjetPanePrensenter;
import timereportfx.service.CategorieService;
import timereportfx.service.ProjetService;
import timereportfx.service.SimpleProjetService;
import timereportfx.service.TimereportJpaController;

/**
 *
 * @author dimi
 */
public class TimeReportFxFactory {

    private ProjetPanePrensenter projetPanePrensenter;
    private MainPresenter mainPresenter;
    
    
    private CategorieService categorieService;
    private ProjetService projetService;
    private TimereportJpaController jpaController;
   
   
    public ProjetPanePrensenter getProjetPanePrensenter() {
        if (projetPanePrensenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("view/ProjetPane.fxml"));
                projetPanePrensenter = (ProjetPanePrensenter) loader.getController();
                projetPanePrensenter.setProjetService(getProjetService());
                //   projetPanePrensenter.setContactDetailPresenter(getContactDetailPresenter());
                //   projetPanePrensenter.setContactSearchPresenter(getContactSearchPresenter());
            } catch (IOException e) {
                throw new RuntimeException("Unable to load ProjetPane.fxml", e);
            }
        }
        return projetPanePrensenter;
    }

    public MainPresenter getMainPresenter() {
        if (projetPanePrensenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();              
                loader.load(getClass().getResourceAsStream("view/TimeReport.fxml"));
                mainPresenter = (MainPresenter) loader.getController();
                mainPresenter.setProjetService(getProjetService());
                //   projetPanePrensenter.setContactDetailPresenter(getContactDetailPresenter());
                //   projetPanePrensenter.setContactSearchPresenter(getContactSearchPresenter());
            } catch (IOException e) {
                throw new RuntimeException("Unable to load TimeReport.fxml", e);
            }
        }
        return mainPresenter;
    }
    

    public CategorieService getCategorieService() {
        return categorieService;
    }

    public ProjetService getProjetService() {
        if (projetService == null) {
            projetService = new SimpleProjetService();
        }
        return projetService;
    }
}
