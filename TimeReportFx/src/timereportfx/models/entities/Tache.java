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
@Table(name = "tache")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tache.findAll", query = "SELECT t FROM Tache t"),
    @NamedQuery(name = "Tache.findByIdtache", query = "SELECT t FROM Tache t WHERE t.idtache = :idtache"),
    @NamedQuery(name = "Tache.findByNom", query = "SELECT t FROM Tache t WHERE t.nom = :nom")})
public class Tache implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idtache")
    private Integer idtache;
    @Column(name = "nom")
    private String nom;
    @JoinColumn(name = "idutilisateur", referencedColumnName = "idutilisateur")
    @ManyToOne
    private Utilisateur idutilisateur;
    @JoinColumn(name = "idprojet", referencedColumnName = "idprojet")
    @ManyToOne
    private Projet idprojet;
    @JoinColumn(name = "cd_ctg", referencedColumnName = "cd_ctg")
    @ManyToOne
    private Categorie cdCtg;
    @OneToMany(mappedBy = "idtache")
    private Collection<Timereport> timereportCollection;

    public Tache() {
    }

    public Tache(Integer idtache) {
        this.idtache = idtache;
    }

    public Integer getIdtache() {
        return idtache;
    }

    public void setIdtache(Integer idtache) {
        this.idtache = idtache;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Utilisateur getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(Utilisateur idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public Projet getIdprojet() {
        return idprojet;
    }

    public void setIdprojet(Projet idprojet) {
        this.idprojet = idprojet;
    }

    public Categorie getCdCtg() {
        return cdCtg;
    }

    public void setCdCtg(Categorie cdCtg) {
        this.cdCtg = cdCtg;
    }

    @XmlTransient
    public Collection<Timereport> getTimereportCollection() {
        return timereportCollection;
    }

    public void setTimereportCollection(Collection<Timereport> timereportCollection) {
        this.timereportCollection = timereportCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtache != null ? idtache.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tache)) {
            return false;
        }
        Tache other = (Tache) object;
        if ((this.idtache == null && other.idtache != null) || (this.idtache != null && !this.idtache.equals(other.idtache))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.Tache[ idtache=" + idtache + " ]";
    }
    
}
