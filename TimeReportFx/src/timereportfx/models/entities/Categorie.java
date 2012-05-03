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
@Table(name = "categorie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categorie.findAll", query = "SELECT c FROM Categorie c"),
    @NamedQuery(name = "Categorie.findByCdCtg", query = "SELECT c FROM Categorie c WHERE c.cdCtg = :cdCtg"),
    @NamedQuery(name = "Categorie.findByLbCtg", query = "SELECT c FROM Categorie c WHERE c.lbCtg = :lbCtg")})
public class Categorie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cd_ctg")
    private String cdCtg;
    @Basic(optional = false)
    @Column(name = "lb_ctg")
    private String lbCtg;
    @OneToMany(mappedBy = "cdCtg")
    private Collection<Tache> tacheCollection;

    public Categorie() {
    }

    public Categorie(String cdCtg) {
        this.cdCtg = cdCtg;
    }

    public Categorie(String cdCtg, String lbCtg) {
        this.cdCtg = cdCtg;
        this.lbCtg = lbCtg;
    }

    public String getCdCtg() {
        return cdCtg;
    }

    public void setCdCtg(String cdCtg) {
        this.cdCtg = cdCtg;
    }

    public String getLbCtg() {
        return lbCtg;
    }

    public void setLbCtg(String lbCtg) {
        this.lbCtg = lbCtg;
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
        hash += (cdCtg != null ? cdCtg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorie)) {
            return false;
        }
        Categorie other = (Categorie) object;
        if ((this.cdCtg == null && other.cdCtg != null) || (this.cdCtg != null && !this.cdCtg.equals(other.cdCtg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.Categorie[ cdCtg=" + cdCtg + " ]";
    }
    
}
