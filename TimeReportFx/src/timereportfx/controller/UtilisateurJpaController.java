/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.models.Groupe;
import timereportfx.models.Tache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
//import javax.transaction.UserTransaction;
import timereportfx.controller.exceptions.NonexistentEntityException;
import timereportfx.controller.exceptions.PreexistingEntityException;
import timereportfx.models.Timereport;
import timereportfx.models.Utilisateur;

/**
 *
 * @author Dimitri Lebel
 */
public class UtilisateurJpaController implements Serializable {

    public UtilisateurJpaController(EntityManagerFactory emf) {
        // this.utx = utx;
        this.emf = emf;
    }
    public UtilisateurJpaController() {
        // this.utx = utx;
        this.emf = timereportfx.TimeReportFx.getEMF();
    }
    //private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Utilisateur utilisateur) throws PreexistingEntityException, Exception {
        if (utilisateur.getTacheCollection() == null) {
            utilisateur.setTacheCollection(new ArrayList<Tache>());
        }
        if (utilisateur.getTimereportCollection() == null) {
            utilisateur.setTimereportCollection(new ArrayList<Timereport>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Groupe idgroupe = utilisateur.getIdgroupe();
            if (idgroupe != null) {
                idgroupe = em.getReference(idgroupe.getClass(), idgroupe.getIdgroupe());
                utilisateur.setIdgroupe(idgroupe);
            }
            Collection<Tache> attachedTacheCollection = new ArrayList<Tache>();
            for (Tache tacheCollectionTacheToAttach : utilisateur.getTacheCollection()) {
                tacheCollectionTacheToAttach = em.getReference(tacheCollectionTacheToAttach.getClass(), tacheCollectionTacheToAttach.getIdtache());
                attachedTacheCollection.add(tacheCollectionTacheToAttach);
            }
            utilisateur.setTacheCollection(attachedTacheCollection);
            Collection<Timereport> attachedTimereportCollection = new ArrayList<Timereport>();
            for (Timereport timereportCollectionTimereportToAttach : utilisateur.getTimereportCollection()) {
                timereportCollectionTimereportToAttach = em.getReference(timereportCollectionTimereportToAttach.getClass(), timereportCollectionTimereportToAttach.getIdtimereport());
                attachedTimereportCollection.add(timereportCollectionTimereportToAttach);
            }
            utilisateur.setTimereportCollection(attachedTimereportCollection);
            em.persist(utilisateur);
            if (idgroupe != null) {
                idgroupe.getUtilisateurCollection().add(utilisateur);
                idgroupe = em.merge(idgroupe);
            }
            for (Tache tacheCollectionTache : utilisateur.getTacheCollection()) {
                Utilisateur oldIdutilisateurOfTacheCollectionTache = tacheCollectionTache.getIdutilisateur();
                tacheCollectionTache.setIdutilisateur(utilisateur);
                tacheCollectionTache = em.merge(tacheCollectionTache);
                if (oldIdutilisateurOfTacheCollectionTache != null) {
                    oldIdutilisateurOfTacheCollectionTache.getTacheCollection().remove(tacheCollectionTache);
                    oldIdutilisateurOfTacheCollectionTache = em.merge(oldIdutilisateurOfTacheCollectionTache);
                }
            }
            for (Timereport timereportCollectionTimereport : utilisateur.getTimereportCollection()) {
                Utilisateur oldIdutilisateurOfTimereportCollectionTimereport = timereportCollectionTimereport.getIdutilisateur();
                timereportCollectionTimereport.setIdutilisateur(utilisateur);
                timereportCollectionTimereport = em.merge(timereportCollectionTimereport);
                if (oldIdutilisateurOfTimereportCollectionTimereport != null) {
                    oldIdutilisateurOfTimereportCollectionTimereport.getTimereportCollection().remove(timereportCollectionTimereport);
                    oldIdutilisateurOfTimereportCollectionTimereport = em.merge(oldIdutilisateurOfTimereportCollectionTimereport);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUtilisateur(utilisateur.getIdutilisateur()) != null) {
                throw new PreexistingEntityException("Utilisateur " + utilisateur + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Utilisateur utilisateur) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilisateur persistentUtilisateur = em.find(Utilisateur.class, utilisateur.getIdutilisateur());
            Groupe idgroupeOld = persistentUtilisateur.getIdgroupe();
            Groupe idgroupeNew = utilisateur.getIdgroupe();
            Collection<Tache> tacheCollectionOld = persistentUtilisateur.getTacheCollection();
            Collection<Tache> tacheCollectionNew = utilisateur.getTacheCollection();
            Collection<Timereport> timereportCollectionOld = persistentUtilisateur.getTimereportCollection();
            Collection<Timereport> timereportCollectionNew = utilisateur.getTimereportCollection();
            if (idgroupeNew != null) {
                idgroupeNew = em.getReference(idgroupeNew.getClass(), idgroupeNew.getIdgroupe());
                utilisateur.setIdgroupe(idgroupeNew);
            }
            Collection<Tache> attachedTacheCollectionNew = new ArrayList<Tache>();
            for (Tache tacheCollectionNewTacheToAttach : tacheCollectionNew) {
                tacheCollectionNewTacheToAttach = em.getReference(tacheCollectionNewTacheToAttach.getClass(), tacheCollectionNewTacheToAttach.getIdtache());
                attachedTacheCollectionNew.add(tacheCollectionNewTacheToAttach);
            }
            tacheCollectionNew = attachedTacheCollectionNew;
            utilisateur.setTacheCollection(tacheCollectionNew);
            Collection<Timereport> attachedTimereportCollectionNew = new ArrayList<Timereport>();
            for (Timereport timereportCollectionNewTimereportToAttach : timereportCollectionNew) {
                timereportCollectionNewTimereportToAttach = em.getReference(timereportCollectionNewTimereportToAttach.getClass(), timereportCollectionNewTimereportToAttach.getIdtimereport());
                attachedTimereportCollectionNew.add(timereportCollectionNewTimereportToAttach);
            }
            timereportCollectionNew = attachedTimereportCollectionNew;
            utilisateur.setTimereportCollection(timereportCollectionNew);
            utilisateur = em.merge(utilisateur);
            if (idgroupeOld != null && !idgroupeOld.equals(idgroupeNew)) {
                idgroupeOld.getUtilisateurCollection().remove(utilisateur);
                idgroupeOld = em.merge(idgroupeOld);
            }
            if (idgroupeNew != null && !idgroupeNew.equals(idgroupeOld)) {
                idgroupeNew.getUtilisateurCollection().add(utilisateur);
                idgroupeNew = em.merge(idgroupeNew);
            }
            for (Tache tacheCollectionOldTache : tacheCollectionOld) {
                if (!tacheCollectionNew.contains(tacheCollectionOldTache)) {
                    tacheCollectionOldTache.setIdutilisateur(null);
                    tacheCollectionOldTache = em.merge(tacheCollectionOldTache);
                }
            }
            for (Tache tacheCollectionNewTache : tacheCollectionNew) {
                if (!tacheCollectionOld.contains(tacheCollectionNewTache)) {
                    Utilisateur oldIdutilisateurOfTacheCollectionNewTache = tacheCollectionNewTache.getIdutilisateur();
                    tacheCollectionNewTache.setIdutilisateur(utilisateur);
                    tacheCollectionNewTache = em.merge(tacheCollectionNewTache);
                    if (oldIdutilisateurOfTacheCollectionNewTache != null && !oldIdutilisateurOfTacheCollectionNewTache.equals(utilisateur)) {
                        oldIdutilisateurOfTacheCollectionNewTache.getTacheCollection().remove(tacheCollectionNewTache);
                        oldIdutilisateurOfTacheCollectionNewTache = em.merge(oldIdutilisateurOfTacheCollectionNewTache);
                    }
                }
            }
            for (Timereport timereportCollectionOldTimereport : timereportCollectionOld) {
                if (!timereportCollectionNew.contains(timereportCollectionOldTimereport)) {
                    timereportCollectionOldTimereport.setIdutilisateur(null);
                    timereportCollectionOldTimereport = em.merge(timereportCollectionOldTimereport);
                }
            }
            for (Timereport timereportCollectionNewTimereport : timereportCollectionNew) {
                if (!timereportCollectionOld.contains(timereportCollectionNewTimereport)) {
                    Utilisateur oldIdutilisateurOfTimereportCollectionNewTimereport = timereportCollectionNewTimereport.getIdutilisateur();
                    timereportCollectionNewTimereport.setIdutilisateur(utilisateur);
                    timereportCollectionNewTimereport = em.merge(timereportCollectionNewTimereport);
                    if (oldIdutilisateurOfTimereportCollectionNewTimereport != null && !oldIdutilisateurOfTimereportCollectionNewTimereport.equals(utilisateur)) {
                        oldIdutilisateurOfTimereportCollectionNewTimereport.getTimereportCollection().remove(timereportCollectionNewTimereport);
                        oldIdutilisateurOfTimereportCollectionNewTimereport = em.merge(oldIdutilisateurOfTimereportCollectionNewTimereport);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = utilisateur.getIdutilisateur();
                if (findUtilisateur(id) == null) {
                    throw new NonexistentEntityException("The utilisateur with id " + id + " no longer exists.");
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
            Utilisateur utilisateur;
            try {
                utilisateur = em.getReference(Utilisateur.class, id);
                utilisateur.getIdutilisateur();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The utilisateur with id " + id + " no longer exists.", enfe);
            }
            Groupe idgroupe = utilisateur.getIdgroupe();
            if (idgroupe != null) {
                idgroupe.getUtilisateurCollection().remove(utilisateur);
                idgroupe = em.merge(idgroupe);
            }
            Collection<Tache> tacheCollection = utilisateur.getTacheCollection();
            for (Tache tacheCollectionTache : tacheCollection) {
                tacheCollectionTache.setIdutilisateur(null);
                tacheCollectionTache = em.merge(tacheCollectionTache);
            }
            Collection<Timereport> timereportCollection = utilisateur.getTimereportCollection();
            for (Timereport timereportCollectionTimereport : timereportCollection) {
                timereportCollectionTimereport.setIdutilisateur(null);
                timereportCollectionTimereport = em.merge(timereportCollectionTimereport);
            }
            em.remove(utilisateur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Utilisateur> findUtilisateurEntities() {
        return findUtilisateurEntities(true, -1, -1);
    }

    public List<Utilisateur> findUtilisateurEntities(int maxResults, int firstResult) {
        return findUtilisateurEntities(false, maxResults, firstResult);
    }

    private List<Utilisateur> findUtilisateurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Utilisateur as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Utilisateur findUtilisateur(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Utilisateur.class, id);
        } finally {
            em.close();
        }
    }

    public int getUtilisateurCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Utilisateur as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Utilisateur findUtilisateurByNom(String nom) {
        Utilisateur u = null;
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Utilisateur.findByNom");
            query.setParameter("nom", nom);
            u = (Utilisateur) query.getSingleResult();
        }
        catch (NoResultException e){
            
        }
        finally {
            em.close();
        }
        return u;
    }
    public boolean isOk(String nom){
        boolean ok =false ;
        if (findUtilisateurByNom(nom) != null)
            ok =true;
        return ok;
    }
}
