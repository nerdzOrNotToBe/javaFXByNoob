/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import timereportfx.gui.projet.ProjetPanePrensenter;
import timereportfx.service.CategorieService;
import timereportfx.service.ProjetService;
import timereportfx.service.SimpleProjetService;

/**
 *
 * @author dimi
 */
public class TimeReportFxFactory {

    private ProjetPanePrensenter projetPanePrensenter;
    private CategorieService categorieService;
    private ProjetService projetService;

    public ProjetPanePrensenter getProjetPanePrensenter() {
        if (projetPanePrensenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("/view/ProjetPane.fxml"));
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
