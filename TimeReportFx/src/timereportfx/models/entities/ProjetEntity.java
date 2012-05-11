/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.models.entities;

import java.io.Serializable;
import java.util.Collection;
import javafx.beans.property.IntegerProperty;
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
    @NamedQuery(name = "Projet.findAll", query = "SELECT p FROM ProjetEntity p"),
    @NamedQuery(name = "Projet.findByIdprojet", query = "SELECT p FROM ProjetEntity p WHERE p.idprojet = :idprojet"),
    @NamedQuery(name = "Projet.findByNom", query = "SELECT p FROM ProjetEntity p WHERE p.nom = :nom"),
    @NamedQuery(name = "Projet.findByCouleur", query = "SELECT p FROM ProjetEntity p WHERE p.couleur = :couleur")})
public class ProjetEntity implements Serializable {
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
    private Collection<TacheEntity> tacheCollection;

    public ProjetEntity() {
    }

    public ProjetEntity(Integer idprojet) {
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
    public Collection<TacheEntity> getTacheCollection() {
        return tacheCollection;
    }

    public void setTacheCollection(Collection<TacheEntity> tacheCollection) {
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
        if (!(object instanceof ProjetEntity)) {
            return false;
        }
        ProjetEntity other = (ProjetEntity) object;
        if ((this.idprojet == null && other.idprojet != null) || (this.idprojet != null && !this.idprojet.equals(other.idprojet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.ProjetEntity[ idprojet=" + idprojet + " ]";
    }
    
}
