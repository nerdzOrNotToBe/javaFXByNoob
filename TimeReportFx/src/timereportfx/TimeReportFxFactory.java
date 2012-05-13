/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import timereportfx.gui.config.ConfigPanePresenter;
import timereportfx.gui.infoBulle.InfoBullePresenter;
import timereportfx.gui.loging.LogingPresenter;
import timereportfx.gui.main.MainPresenter;
import timereportfx.gui.projet.ProjetPanePrensenter;
import timereportfx.service.*;

/**
 *
 * @author dimi
 */
public class TimeReportFxFactory {

    private ProjetPanePrensenter projetPanePrensenter;
    private MainPresenter mainPresenter;
    private LogingPresenter logingPresenter;
    private InfoBullePresenter infoBullePresenter;
    private CategorieService categorieService;
    private ProjetService projetService;
    private ConfigPanePresenter configPanePresenter;
    private TimereportJpaController jpaController;
    private UtilisateurService utilisateurService;

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
        if (mainPresenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("view/TimeReport.fxml"));
                mainPresenter = (MainPresenter) loader.getController();
                mainPresenter.setProjetService(getProjetService());
            } catch (IOException e) {
                throw new RuntimeException("Unable to load TimeReport.fxml", e);
            }
        }
        return mainPresenter;
    }

    public LogingPresenter getLogingPresenter() {
        if (logingPresenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("view/Login.fxml"));
                logingPresenter = (LogingPresenter) loader.getController();
            } catch (IOException e) {
                throw new RuntimeException("Unable to load Login.fxml", e);
            }
        }
        return logingPresenter;
    }

    public InfoBullePresenter getInfoBullePresenter() {
        if (logingPresenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("view/InfoBulle.fxml"));
                logingPresenter = (LogingPresenter) loader.getController();
            } catch (IOException e) {
                throw new RuntimeException("Unable to load InfoBulle.fxml", e);
            }
        }
        return infoBullePresenter;
    }

    public ConfigPanePresenter getConfigPanePresenter() {
        if (configPanePresenter == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("view/ConfigPane.fxml"));
                configPanePresenter = (ConfigPanePresenter) loader.getController();
            } catch (IOException e) {
                throw new RuntimeException("Unable to load ConfigPane.fxml", e);
            }
        }
        return configPanePresenter;
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

    public UtilisateurService getUtilisateurService() {
        if (utilisateurService == null) {
            utilisateurService = new SimpleUtilisateurService();
        }
        return utilisateurService;
    }
}
