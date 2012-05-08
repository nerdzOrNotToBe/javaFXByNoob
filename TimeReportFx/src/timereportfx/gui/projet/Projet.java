/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.gui.projet;

import java.util.Collection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import timereportfx.models.Tache;

/**
 *
 * @author dimi
 */
public class Projet {

    private IntegerProperty idprojet;
    private StringProperty nom;
    private StringProperty couleur;
    private Collection<Tache> tacheCollection;

    public Projet() {
        this.idprojet.set(0);
        this.nom.set("");
        this.couleur.set("");
    }

    public String getCouleur() {
        return couleur.get();
    }

    public void setCouleur(String couleur) {
        this.couleur.set(couleur);
    }

    public Integer getIdprojet() {
        return idprojet.get();
    }

    public void setIdprojet(Integer idprojet) {
        this.idprojet.set(idprojet);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public Collection<Tache> getTacheCollection() {
        return tacheCollection;
    }

    public void setTacheCollection(Collection<Tache> tacheCollection) {
        this.tacheCollection = tacheCollection;
    }

    public StringProperty getCouleurProperty() {
        return couleur;
    }

    public IntegerProperty getIdprojetpProperty() {
        return idprojet;
    }

    public StringProperty getNomProperty() {
        return nom;
    }
}
