/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;


import java.awt.Color;
import java.net.URL;
import java.util.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import timereportfx.models.Projet;
import timereportfx.models.Tache;

/**
 *
 * @author Dimitri Lebel
 */
public class ProjetController implements Initializable {

    @FXML
    private VBox vbox;
    private Projet projet;
    private List<Tache> taches;
    private ToggleGroup tg;
    private double btnWidth;
    private Main main;
    private ToggleButton btnPressed;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<Tache> getTaches() {
        return taches;
    }

    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    public ToggleGroup getTg() {
        return tg;
    }

    public void setTg(ToggleGroup tg) {
        this.tg = tg;
    }

    public void setBtnWidth(double btnWidth) {
        this.btnWidth = btnWidth;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        taches = new ArrayList<Tache>();
    }

    public void AfficheTache() {
        int i = 0;
        Collection<Tache> tacheCollection = projet.getTacheCollection();
        vbox.setSpacing(1);
        Iterator iteratorTache = tacheCollection.iterator();
        while (iteratorTache.hasNext()) {
            Tache tache_temp = (Tache) iteratorTache.next();
            taches.add(tache_temp);
            ToggleButton btn = new ToggleButton(tache_temp.getNom());
            btn.getStyleClass().add("ToggleButton");
            if (projet.getCouleur() != null) {
                btn.setStyle("-fx-base: " + getColorHTML(new Integer(projet.getCouleur())) + ";" + "-fx-background-radius: 0, 0, -1, -2;");
            }
            btn.setPrefWidth(btnWidth);
            btn.setToggleGroup(tg);
            btn.setId(i + projet.getNom());
            i++;
            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    if (!arg0.isConsumed()) {
                        if (btnPressed == null) {
                            ToggleButton btn = (ToggleButton) arg0.getSource();
                            Integer j = new Integer(btn.getId().substring(0, 1));
                            btnPressed = btn; 
                            main.setBt(btn);
                            main.startTache(taches.get(j));
                        } else if (btnPressed == (ToggleButton) arg0.getSource()) {
                            main.stopTache();
                            btnPressed = null;
                        } else {
                            main.stopTache();
                            ToggleButton btn = (ToggleButton) arg0.getSource();
                            Integer j = new Integer(btn.getId().substring(0, 1));
                            btnPressed = btn;
                            main.setBt(btn);
                            main.startTache(taches.get(j));
                        }
                    }
                }
            });
            vbox.getChildren().add(btn);
        }
    }

    public String getColorHTML(int couleurWindev) {
        int b = couleurWindev / 65536;
        int v = (couleurWindev - (65536 * b)) / 256;
        int r = couleurWindev - ((65536 * b) + (256 * v));
        Color c = new Color(r, v, b);
        String couleur = Integer.toHexString(c.getRGB());
        couleur = "#" + couleur.substring(2, couleur.length());
        return couleur;
    }
}
