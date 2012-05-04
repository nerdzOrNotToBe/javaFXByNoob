/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.TimeReportFx;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.UtilisateurEntity;
import timereportfx.models.entities.TacheEntity;
import timereportfx.models.entities.TimereportEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class TimereportJpaController implements Serializable {

    public TimereportJpaController( EntityManagerFactory emf) {
   //     this.utx = utx;
        this.emf = emf;
    }
   // private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public TimereportJpaController() {
        emf = TimeReportFx.getEMF();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TimereportEntity timereport) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UtilisateurEntity idutilisateur = timereport.getIdutilisateur();
            if (idutilisateur != null) {
                idutilisateur = em.getReference(idutilisateur.getClass(), idutilisateur.getIdutilisateur());
                timereport.setIdutilisateur(idutilisateur);
            }
            TacheEntity idtache = timereport.getIdtache();
            if (idtache != null) {
                idtache = em.getReference(idtache.getClass(), idtache.getIdtache());
                timereport.setIdtache(idtache);
            }
            em.persist(timereport);
            if (idutilisateur != null) {
                idutilisateur.getTimereportCollection().add(timereport);
                idutilisateur = em.merge(idutilisateur);
            }
            if (idtache != null) {
                idtache.getTimereportCollection().add(timereport);
                idtache = em.merge(idtache);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTimereport(timereport.getIdtimereport()) != null) {
                throw new PreexistingEntityException("Timereport " + timereport + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TimereportEntity timereport) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TimereportEntity persistentTimereport = em.find(TimereportEntity.class, timereport.getIdtimereport());
            UtilisateurEntity idutilisateurOld = persistentTimereport.getIdutilisateur();
            UtilisateurEntity idutilisateurNew = timereport.getIdutilisateur();
            TacheEntity idtacheOld = persistentTimereport.getIdtache();
            TacheEntity idtacheNew = timereport.getIdtache();
            if (idutilisateurNew != null) {
                idutilisateurNew = em.getReference(idutilisateurNew.getClass(), idutilisateurNew.getIdutilisateur());
                timereport.setIdutilisateur(idutilisateurNew);
            }
            if (idtacheNew != null) {
                idtacheNew = em.getReference(idtacheNew.getClass(), idtacheNew.getIdtache());
                timereport.setIdtache(idtacheNew);
            }
            timereport = em.merge(timereport);
            if (idutilisateurOld != null && !idutilisateurOld.equals(idutilisateurNew)) {
                idutilisateurOld.getTimereportCollection().remove(timereport);
                idutilisateurOld = em.merge(idutilisateurOld);
            }
            if (idutilisateurNew != null && !idutilisateurNew.equals(idutilisateurOld)) {
                idutilisateurNew.getTimereportCollection().add(timereport);
                idutilisateurNew = em.merge(idutilisateurNew);
            }
            if (idtacheOld != null && !idtacheOld.equals(idtacheNew)) {
                idtacheOld.getTimereportCollection().remove(timereport);
                idtacheOld = em.merge(idtacheOld);
            }
            if (idtacheNew != null && !idtacheNew.equals(idtacheOld)) {
                idtacheNew.getTimereportCollection().add(timereport);
                idtacheNew = em.merge(idtacheNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = timereport.getIdtimereport();
                if (findTimereport(id) == null) {
                    throw new NonexistentEntityException("The timereport with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TimereportEntity timereport;
            try {
                timereport = em.getReference(TimereportEntity.class, id);
                timereport.getIdtimereport();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The timereport with id " + id + " no longer exists.", enfe);
            }
            UtilisateurEntity idutilisateur = timereport.getIdutilisateur();
            if (idutilisateur != null) {
                idutilisateur.getTimereportCollection().remove(timereport);
                idutilisateur = em.merge(idutilisateur);
            }
            TacheEntity idtache = timereport.getIdtache();
            if (idtache != null) {
                idtache.getTimereportCollection().remove(timereport);
                idtache = em.merge(idtache);
            }
            em.remove(timereport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TimereportEntity> findTimereportEntities() {
        return findTimereportEntities(true, -1, -1);
    }

    public List<TimereportEntity> findTimereportEntities(int maxResults, int firstResult) {
        return findTimereportEntities(false, maxResults, firstResult);
    }

    private List<TimereportEntity> findTimereportEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from TimereportEntity as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TimereportEntity findTimereport(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TimereportEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getTimereportCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from TimereportEntity as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
