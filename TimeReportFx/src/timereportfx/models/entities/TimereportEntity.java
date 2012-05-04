/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dimitri Lebel
 */
@Entity
@Table(name = "timereport")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Timereport.findAll", query = "SELECT t FROM TimereportEntity t"),
    @NamedQuery(name = "Timereport.findByIdtimereport", query = "SELECT t FROM TimereportEntity t WHERE t.idtimereport = :idtimereport"),
    @NamedQuery(name = "Timereport.findByTsDebut", query = "SELECT t FROM TimereportEntity t WHERE t.tsDebut = :tsDebut"),
    @NamedQuery(name = "Timereport.findByTsFin", query = "SELECT t FROM TimereportEntity t WHERE t.tsFin = :tsFin"),
    @NamedQuery(name = "Timereport.findByDuree", query = "SELECT t FROM TimereportEntity t WHERE t.duree = :duree")})
public class TimereportEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    @Basic(optional = false)
    @Column(name = "idtimereport")
    private Integer idtimereport;
    @Column(name = "ts_debut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tsDebut;
    @Column(name = "ts_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tsFin;
    @Column(name = "duree")
    private Integer duree;
    @JoinColumn(name = "idutilisateur", referencedColumnName = "idutilisateur")
    @ManyToOne
    private UtilisateurEntity idutilisateur;
    @JoinColumn(name = "idtache", referencedColumnName = "idtache")
    @ManyToOne
    private TacheEntity idtache;

    public TimereportEntity() {
    }

    public TimereportEntity(Integer idtimereport) {
        this.idtimereport = idtimereport;
    }

    public Integer getIdtimereport() {
        return idtimereport;
    }

    public void setIdtimereport(Integer idtimereport) {
        this.idtimereport = idtimereport;
    }

    public Date getTsDebut() {
        return tsDebut;
    }

    public void setTsDebut(Date tsDebut) {
        this.tsDebut = tsDebut;
    }

    public Date getTsFin() {
        return tsFin;
    }

    public void setTsFin(Date tsFin) {
        this.tsFin = tsFin;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public UtilisateurEntity getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(UtilisateurEntity idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public TacheEntity getIdtache() {
        return idtache;
    }

    public void setIdtache(TacheEntity idtache) {
        this.idtache = idtache;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtimereport != null ? idtimereport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimereportEntity)) {
            return false;
        }
        TimereportEntity other = (TimereportEntity) object;
        if ((this.idtimereport == null && other.idtimereport != null) || (this.idtimereport != null && !this.idtimereport.equals(other.idtimereport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "timereportfx.models.TimereportEntity[ idtimereport=" + idtimereport + " ]";
    }
    
}
