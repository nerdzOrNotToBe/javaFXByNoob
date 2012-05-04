/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dimitri Lebel
 */
public class TimeReport {

    private IntegerProperty idtimereport;
    private StringProperty tsDebut;
    private StringProperty tsFin;
    private IntegerProperty duree;
    private Utilisateur utilisateur;
    private Tache tache;

    public TimeReport() {
        idtimereport = new SimpleIntegerProperty(-1);
        tsDebut = new SimpleStringProperty("");
        tsFin = new SimpleStringProperty("");
        utilisateur = new Utilisateur();
        tache = new Tache();
    }

    public IntegerProperty DureeProperty() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree.set(duree);
    }

    public Integer getDuree() {
        return duree.get();
    }

    public IntegerProperty IdtimereportProperty() {
        return idtimereport;
    }

    public Integer getIdtimereport() {
        return idtimereport.get();
    }

    public void setIdtimereport(int idtimereport) {
        this.idtimereport.set(idtimereport);
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public StringProperty getTsDebut() {
        return tsDebut;
    }

    public void setTsDebut(StringProperty tsDebut) {
        this.tsDebut = tsDebut;
    }

    public StringProperty getTsFin() {
        return tsFin;
    }

    public void setTsFin(StringProperty tsFin) {
        this.tsFin = tsFin;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
