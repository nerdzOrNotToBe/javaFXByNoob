/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dimitri Lebel
 */
@Entity
@Table(name = "utilisateur")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM Utilisateur u"),
    @NamedQuery(name = "Utilisateur.findByIdutilisateur", query = "SELECT u FROM Utilisateur u WHERE u.idutilisateur = :idutilisateur"),
    @NamedQuery(name = "Utilisateur.findByNom", query = "SELECT u FROM Utilisateur u WHERE u.nom = :nom"),
    @NamedQuery(name = "Utilisateur.findByPrenom", query = "SELECT u FROM Utilisateur u WHERE u.prenom = :prenom"),
    @NamedQuery(name = "Utilisateur.findByDebutJ", query = "SELECT u FROM Utilisateur u WHERE u.debutJ = :debutJ"),
    @NamedQuery(name = "Utilisateur.findByFinJ", query = "SELECT u FROM Utilisateur u WHERE u.finJ = :finJ"),
    @NamedQuery(name = "Utilisateur.findByDebutRepas", query = "SELECT u FROM Utilisateur u WHERE u.debutRepas = :debutRepas"),
    @NamedQuery(name = "Utilisateur.findByFinRepas", query = "SELECT u FROM Utilisateur u WHERE u.finRepas = :finRepas")})
public class Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idutilisateur")
    private Integer idutilisateur;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "debut_j")
    @Temporal(TemporalType.TIME)
    private Date debutJ;
    @Column(name = "fin_j")
    @Temporal(TemporalType.TIME)
    private Date finJ;
    @Column(name = "debut_repas")
    @Temporal(TemporalType.TIME)
    private Date debutRepas;
    @Column(name = "fin_repas")
    @Temporal(TemporalType.TIME)
    private Date finRepas;
    @OneToMany(mappedBy = "idutilisateur")
    private Collection<Tache> tacheCollection;
    @OneToMany(mappedBy = "idutilisateur")
    private Collection<Timereport> timereportCollection;
    @JoinColumn(name = "idgroupe", referencedColumnName = "idgroupe")
    @ManyToOne
    private Groupe idgroupe;

    public Utilisateur() {
    }

    public Utilisateur(Integer idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public Integer getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(Integer idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDebutJ() {
        return debutJ;
    }

    public void setDebutJ(Date debutJ) {
        this.debutJ = debutJ;
    }

    public Date getFinJ() {
        return finJ;
    }

    public void setFinJ(Date finJ) {
        this.finJ = finJ;
    }

    public Date getDebutRepas() {
        return debutRepas;
    }

    public void setDebutRepas(Date debutRepas) {
        this.debutRepas = debutRepas;
    }

    public Date getFinRepas() {
        return finRepas;
    }

    public void setFinRepas(Date finRepas) {
        this.finRepas = finRepas;
    }

    @XmlTransient
    public Collection<Tache> getTacheCollection() {
        return tacheCollection;
    }

    public void setTacheCollection(Collection<Tache> tacheCollection) {
        this.tacheCollection = tacheCollection;
    }

    @XmlTransient
    public Collection<Timereport> getTimereportCollection() {
        return timereportCollection;
    }

    public void setTimereportCollection(Collection<Timereport> timereportCollection) {
        this.timereportCollection = timereportCollection;
    }

    public Groupe getIdgroupe() {
        return idgroupe;
    }

    public void setIdgroupe(Groupe idgroupe) {
        this.idgroupe = idgroupe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idutilisateur != null ? idutilisateur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.idutilisateur == null && other.idutilisateur != null) || (this.idutilisateur != null && !this.idutilisateur.equals(other.idutilisateur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.Utilisateur[ idutilisateur=" + idutilisateur + " ]";
    }
    
}
