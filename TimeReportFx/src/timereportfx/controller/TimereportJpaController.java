/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.TimeReportFx;
//import javax.transaction.UserTransaction;
import timereportfx.controller.exceptions.NonexistentEntityException;
import timereportfx.controller.exceptions.PreexistingEntityException;
import timereportfx.models.Utilisateur;
import timereportfx.models.Tache;
import timereportfx.models.Timereport;

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

    TimereportJpaController() {
        emf = TimeReportFx.getEMF();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Timereport timereport) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilisateur idutilisateur = timereport.getIdutilisateur();
            if (idutilisateur != null) {
                idutilisateur = em.getReference(idutilisateur.getClass(), idutilisateur.getIdutilisateur());
                timereport.setIdutilisateur(idutilisateur);
            }
            Tache idtache = timereport.getIdtache();
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

    public void edit(Timereport timereport) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Timereport persistentTimereport = em.find(Timereport.class, timereport.getIdtimereport());
            Utilisateur idutilisateurOld = persistentTimereport.getIdutilisateur();
            Utilisateur idutilisateurNew = timereport.getIdutilisateur();
            Tache idtacheOld = persistentTimereport.getIdtache();
            Tache idtacheNew = timereport.getIdtache();
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
            Timereport timereport;
            try {
                timereport = em.getReference(Timereport.class, id);
                timereport.getIdtimereport();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The timereport with id " + id + " no longer exists.", enfe);
            }
            Utilisateur idutilisateur = timereport.getIdutilisateur();
            if (idutilisateur != null) {
                idutilisateur.getTimereportCollection().remove(timereport);
                idutilisateur = em.merge(idutilisateur);
            }
            Tache idtache = timereport.getIdtache();
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

    public List<Timereport> findTimereportEntities() {
        return findTimereportEntities(true, -1, -1);
    }

    public List<Timereport> findTimereportEntities(int maxResults, int firstResult) {
        return findTimereportEntities(false, maxResults, firstResult);
    }

    private List<Timereport> findTimereportEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Timereport as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Timereport findTimereport(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Timereport.class, id);
        } finally {
            em.close();
        }
    }

    public int getTimereportCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Timereport as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
