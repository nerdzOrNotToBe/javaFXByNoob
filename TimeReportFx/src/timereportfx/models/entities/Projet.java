/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.models.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dimitri Lebel
 */
@Entity
@Table(name = "projet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Projet.findAll", query = "SELECT p FROM Projet p"),
    @NamedQuery(name = "Projet.findByIdprojet", query = "SELECT p FROM Projet p WHERE p.idprojet = :idprojet"),
    @NamedQuery(name = "Projet.findByNom", query = "SELECT p FROM Projet p WHERE p.nom = :nom"),
    @NamedQuery(name = "Projet.findByCouleur", query = "SELECT p FROM Projet p WHERE p.couleur = :couleur")})
public class Projet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idprojet")
    private Integer idprojet;
    @Column(name = "nom")
    private String nom;
    @Column(name = "couleur")
    private String couleur;
    @OneToMany(mappedBy = "idprojet")
    private Collection<Tache> tacheCollection;

    public Projet() {
    }

    public Projet(Integer idprojet) {
        this.idprojet = idprojet;
    }

    public Integer getIdprojet() {
        return idprojet;
    }

    public void setIdprojet(Integer idprojet) {
        this.idprojet = idprojet;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @XmlTransient
    public Collection<Tache> getTacheCollection() {
        return tacheCollection;
    }

    public void setTacheCollection(Collection<Tache> tacheCollection) {
        this.tacheCollection = tacheCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idprojet != null ? idprojet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Projet)) {
            return false;
        }
        Projet other = (Projet) object;
        if ((this.idprojet == null && other.idprojet != null) || (this.idprojet != null && !this.idprojet.equals(other.idprojet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.Projet[ idprojet=" + idprojet + " ]";
    }
    
}
