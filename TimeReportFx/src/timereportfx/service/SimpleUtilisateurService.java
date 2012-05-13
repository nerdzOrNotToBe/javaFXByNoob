/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.models.entities.GroupeEntity;
import timereportfx.models.entities.TacheEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.TimereportEntity;
import timereportfx.models.entities.UtilisateurEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class SimpleUtilisateurService implements UtilisateurService {

    public SimpleUtilisateurService(EntityManagerFactory emf) {
        // this.utx = utx;
        this.emf = emf;
    }
    public SimpleUtilisateurService() {
        // this.utx = utx;
        this.emf = timereportfx.TimeReportFx.getEMF();
    }
    //private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(UtilisateurEntity utilisateur) throws PreexistingEntityException, Exception {
        if (utilisateur.getTacheCollection() == null) {
            utilisateur.setTacheCollection(new ArrayList<TacheEntity>());
        }
        if (utilisateur.getTimereportCollection() == null) {
            utilisateur.setTimereportCollection(new ArrayList<TimereportEntity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GroupeEntity idgroupe = utilisateur.getIdgroupe();
            if (idgroupe != null) {
                idgroupe = em.getReference(idgroupe.getClass(), idgroupe.getIdgroupe());
                utilisateur.setIdgroupe(idgroupe);
            }
            Collection<TacheEntity> attachedTacheCollection = new ArrayList<TacheEntity>();
            for (TacheEntity tacheCollectionTacheToAttach : utilisateur.getTacheCollection()) {
                tacheCollectionTacheToAttach = em.getReference(tacheCollectionTacheToAttach.getClass(), tacheCollectionTacheToAttach.getIdtache());
                attachedTacheCollection.add(tacheCollectionTacheToAttach);
            }
            utilisateur.setTacheCollection(attachedTacheCollection);
            Collection<TimereportEntity> attachedTimereportCollection = new ArrayList<TimereportEntity>();
            for (TimereportEntity timereportCollectionTimereportToAttach : utilisateur.getTimereportCollection()) {
                timereportCollectionTimereportToAttach = em.getReference(timereportCollectionTimereportToAttach.getClass(), timereportCollectionTimereportToAttach.getIdtimereport());
                attachedTimereportCollection.add(timereportCollectionTimereportToAttach);
            }
            utilisateur.setTimereportCollection(attachedTimereportCollection);
            em.persist(utilisateur);
            if (idgroupe != null) {
                idgroupe.getUtilisateurCollection().add(utilisateur);
                idgroupe = em.merge(idgroupe);
            }
            for (TacheEntity tacheCollectionTache : utilisateur.getTacheCollection()) {
                UtilisateurEntity oldIdutilisateurOfTacheCollectionTache = tacheCollectionTache.getIdutilisateur();
                tacheCollectionTache.setIdutilisateur(utilisateur);
                tacheCollectionTache = em.merge(tacheCollectionTache);
                if (oldIdutilisateurOfTacheCollectionTache != null) {
                    oldIdutilisateurOfTacheCollectionTache.getTacheCollection().remove(tacheCollectionTache);
                    oldIdutilisateurOfTacheCollectionTache = em.merge(oldIdutilisateurOfTacheCollectionTache);
                }
            }
            for (TimereportEntity timereportCollectionTimereport : utilisateur.getTimereportCollection()) {
                UtilisateurEntity oldIdutilisateurOfTimereportCollectionTimereport = timereportCollectionTimereport.getIdutilisateur();
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

    @Override
    public void edit(UtilisateurEntity utilisateur) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UtilisateurEntity persistentUtilisateur = em.find(UtilisateurEntity.class, utilisateur.getIdutilisateur());
            GroupeEntity idgroupeOld = persistentUtilisateur.getIdgroupe();
            GroupeEntity idgroupeNew = utilisateur.getIdgroupe();
            Collection<TacheEntity> tacheCollectionOld = persistentUtilisateur.getTacheCollection();
            Collection<TacheEntity> tacheCollectionNew = utilisateur.getTacheCollection();
            Collection<TimereportEntity> timereportCollectionOld = persistentUtilisateur.getTimereportCollection();
            Collection<TimereportEntity> timereportCollectionNew = utilisateur.getTimereportCollection();
            if (idgroupeNew != null) {
                idgroupeNew = em.getReference(idgroupeNew.getClass(), idgroupeNew.getIdgroupe());
                utilisateur.setIdgroupe(idgroupeNew);
            }
            Collection<TacheEntity> attachedTacheCollectionNew = new ArrayList<TacheEntity>();
            for (TacheEntity tacheCollectionNewTacheToAttach : tacheCollectionNew) {
                tacheCollectionNewTacheToAttach = em.getReference(tacheCollectionNewTacheToAttach.getClass(), tacheCollectionNewTacheToAttach.getIdtache());
                attachedTacheCollectionNew.add(tacheCollectionNewTacheToAttach);
            }
            tacheCollectionNew = attachedTacheCollectionNew;
            utilisateur.setTacheCollection(tacheCollectionNew);
            Collection<TimereportEntity> attachedTimereportCollectionNew = new ArrayList<TimereportEntity>();
            for (TimereportEntity timereportCollectionNewTimereportToAttach : timereportCollectionNew) {
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
            for (TacheEntity tacheCollectionOldTache : tacheCollectionOld) {
                if (!tacheCollectionNew.contains(tacheCollectionOldTache)) {
                    tacheCollectionOldTache.setIdutilisateur(null);
                    tacheCollectionOldTache = em.merge(tacheCollectionOldTache);
                }
            }
            for (TacheEntity tacheCollectionNewTache : tacheCollectionNew) {
                if (!tacheCollectionOld.contains(tacheCollectionNewTache)) {
                    UtilisateurEntity oldIdutilisateurOfTacheCollectionNewTache = tacheCollectionNewTache.getIdutilisateur();
                    tacheCollectionNewTache.setIdutilisateur(utilisateur);
                    tacheCollectionNewTache = em.merge(tacheCollectionNewTache);
                    if (oldIdutilisateurOfTacheCollectionNewTache != null && !oldIdutilisateurOfTacheCollectionNewTache.equals(utilisateur)) {
                        oldIdutilisateurOfTacheCollectionNewTache.getTacheCollection().remove(tacheCollectionNewTache);
                        oldIdutilisateurOfTacheCollectionNewTache = em.merge(oldIdutilisateurOfTacheCollectionNewTache);
                    }
                }
            }
            for (TimereportEntity timereportCollectionOldTimereport : timereportCollectionOld) {
                if (!timereportCollectionNew.contains(timereportCollectionOldTimereport)) {
                    timereportCollectionOldTimereport.setIdutilisateur(null);
                    timereportCollectionOldTimereport = em.merge(timereportCollectionOldTimereport);
                }
            }
            for (TimereportEntity timereportCollectionNewTimereport : timereportCollectionNew) {
                if (!timereportCollectionOld.contains(timereportCollectionNewTimereport)) {
                    UtilisateurEntity oldIdutilisateurOfTimereportCollectionNewTimereport = timereportCollectionNewTimereport.getIdutilisateur();
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

    @Override
    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UtilisateurEntity utilisateur;
            try {
                utilisateur = em.getReference(UtilisateurEntity.class, id);
                utilisateur.getIdutilisateur();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The utilisateur with id " + id + " no longer exists.", enfe);
            }
            GroupeEntity idgroupe = utilisateur.getIdgroupe();
            if (idgroupe != null) {
                idgroupe.getUtilisateurCollection().remove(utilisateur);
                idgroupe = em.merge(idgroupe);
            }
            Collection<TacheEntity> tacheCollection = utilisateur.getTacheCollection();
            for (TacheEntity tacheCollectionTache : tacheCollection) {
                tacheCollectionTache.setIdutilisateur(null);
                tacheCollectionTache = em.merge(tacheCollectionTache);
            }
            Collection<TimereportEntity> timereportCollection = utilisateur.getTimereportCollection();
            for (TimereportEntity timereportCollectionTimereport : timereportCollection) {
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

    @Override
    public List<UtilisateurEntity> findUtilisateurEntities() {
        return findUtilisateurEntities(true, -1, -1);
    }

    public List<UtilisateurEntity> findUtilisateurEntities(int maxResults, int firstResult) {
        return findUtilisateurEntities(false, maxResults, firstResult);
    }

    private List<UtilisateurEntity> findUtilisateurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from UtilisateurEntity as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public UtilisateurEntity findUtilisateur(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UtilisateurEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getUtilisateurCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from UtilisateurEntity as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public UtilisateurEntity findUtilisateurByNom(String nom) {
        UtilisateurEntity u = null;
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Utilisateur.findByNom");
            query.setParameter("nom", nom);
            u = (UtilisateurEntity) query.getSingleResult();
        }
        catch (NoResultException e){
            
        }
        finally {
            em.close();
        }
        return u;
    }
    @Override
    public boolean isOk(String nom){
        boolean ok =false ;
        if (findUtilisateurByNom(nom) != null)
            ok =true;
        return ok;
    }
}
