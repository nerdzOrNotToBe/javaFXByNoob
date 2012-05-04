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
    @NamedQuery(name = "Tache.findAll", query = "SELECT t FROM TacheEntity t"),
    @NamedQuery(name = "Tache.findByIdtache", query = "SELECT t FROM TacheEntity t WHERE t.idtache = :idtache"),
    @NamedQuery(name = "Tache.findByNom", query = "SELECT t FROM TacheEntity t WHERE t.nom = :nom")})
public class TacheEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idtache")
    private Integer idtache;
    @Column(name = "nom")
    private String nom;
    @JoinColumn(name = "idutilisateur", referencedColumnName = "idutilisateur")
    @ManyToOne
    private UtilisateurEntity idutilisateur;
    @JoinColumn(name = "idprojet", referencedColumnName = "idprojet")
    @ManyToOne
    private ProjetEntity idprojet;
    @JoinColumn(name = "cd_ctg", referencedColumnName = "cd_ctg")
    @ManyToOne
    private CategorieEntity cdCtg;
    @OneToMany(mappedBy = "idtache")
    private Collection<TimereportEntity> timereportCollection;

    public TacheEntity() {
    }

    public TacheEntity(Integer idtache) {
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

    public UtilisateurEntity getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(UtilisateurEntity idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public ProjetEntity getIdprojet() {
        return idprojet;
    }

    public void setIdprojet(ProjetEntity idprojet) {
        this.idprojet = idprojet;
    }

    public CategorieEntity getCdCtg() {
        return cdCtg;
    }

    public void setCdCtg(CategorieEntity cdCtg) {
        this.cdCtg = cdCtg;
    }

    @XmlTransient
    public Collection<TimereportEntity> getTimereportCollection() {
        return timereportCollection;
    }

    public void setTimereportCollection(Collection<TimereportEntity> timereportCollection) {
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
        if (!(object instanceof TacheEntity)) {
            return false;
        }
        TacheEntity other = (TacheEntity) object;
        if ((this.idtache == null && other.idtache != null) || (this.idtache != null && !this.idtache.equals(other.idtache))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.TacheEntity[ idtache=" + idtache + " ]";
    }
    
}
