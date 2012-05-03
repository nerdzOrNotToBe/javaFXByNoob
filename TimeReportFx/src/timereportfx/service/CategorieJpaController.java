/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.models.entities.Tache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.Categorie;

/**
 *
 * @author Dimitri Lebel
 */
public class CategorieJpaController implements Serializable {

    public CategorieJpaController( EntityManagerFactory emf) {
      //  this.utx = utx;
        this.emf = emf;
    }
    //private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categorie categorie) throws PreexistingEntityException, Exception {
        if (categorie.getTacheCollection() == null) {
            categorie.setTacheCollection(new ArrayList<Tache>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Tache> attachedTacheCollection = new ArrayList<Tache>();
            for (Tache tacheCollectionTacheToAttach : categorie.getTacheCollection()) {
                tacheCollectionTacheToAttach = em.getReference(tacheCollectionTacheToAttach.getClass(), tacheCollectionTacheToAttach.getIdtache());
                attachedTacheCollection.add(tacheCollectionTacheToAttach);
            }
            categorie.setTacheCollection(attachedTacheCollection);
            em.persist(categorie);
            for (Tache tacheCollectionTache : categorie.getTacheCollection()) {
                Categorie oldCdCtgOfTacheCollectionTache = tacheCollectionTache.getCdCtg();
                tacheCollectionTache.setCdCtg(categorie);
                tacheCollectionTache = em.merge(tacheCollectionTache);
                if (oldCdCtgOfTacheCollectionTache != null) {
                    oldCdCtgOfTacheCollectionTache.getTacheCollection().remove(tacheCollectionTache);
                    oldCdCtgOfTacheCollectionTache = em.merge(oldCdCtgOfTacheCollectionTache);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategorie(categorie.getCdCtg()) != null) {
                throw new PreexistingEntityException("Categorie " + categorie + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categorie categorie) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorie persistentCategorie = em.find(Categorie.class, categorie.getCdCtg());
            Collection<Tache> tacheCollectionOld = persistentCategorie.getTacheCollection();
            Collection<Tache> tacheCollectionNew = categorie.getTacheCollection();
            Collection<Tache> attachedTacheCollectionNew = new ArrayList<Tache>();
            for (Tache tacheCollectionNewTacheToAttach : tacheCollectionNew) {
                tacheCollectionNewTacheToAttach = em.getReference(tacheCollectionNewTacheToAttach.getClass(), tacheCollectionNewTacheToAttach.getIdtache());
                attachedTacheCollectionNew.add(tacheCollectionNewTacheToAttach);
            }
            tacheCollectionNew = attachedTacheCollectionNew;
            categorie.setTacheCollection(tacheCollectionNew);
            categorie = em.merge(categorie);
            for (Tache tacheCollectionOldTache : tacheCollectionOld) {
                if (!tacheCollectionNew.contains(tacheCollectionOldTache)) {
                    tacheCollectionOldTache.setCdCtg(null);
                    tacheCollectionOldTache = em.merge(tacheCollectionOldTache);
                }
            }
            for (Tache tacheCollectionNewTache : tacheCollectionNew) {
                if (!tacheCollectionOld.contains(tacheCollectionNewTache)) {
                    Categorie oldCdCtgOfTacheCollectionNewTache = tacheCollectionNewTache.getCdCtg();
                    tacheCollectionNewTache.setCdCtg(categorie);
                    tacheCollectionNewTache = em.merge(tacheCollectionNewTache);
                    if (oldCdCtgOfTacheCollectionNewTache != null && !oldCdCtgOfTacheCollectionNewTache.equals(categorie)) {
                        oldCdCtgOfTacheCollectionNewTache.getTacheCollection().remove(tacheCollectionNewTache);
                        oldCdCtgOfTacheCollectionNewTache = em.merge(oldCdCtgOfTacheCollectionNewTache);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = categorie.getCdCtg();
                if (findCategorie(id) == null) {
                    throw new NonexistentEntityException("The categorie with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorie categorie;
            try {
                categorie = em.getReference(Categorie.class, id);
                categorie.getCdCtg();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorie with id " + id + " no longer exists.", enfe);
            }
            Collection<Tache> tacheCollection = categorie.getTacheCollection();
            for (Tache tacheCollectionTache : tacheCollection) {
                tacheCollectionTache.setCdCtg(null);
                tacheCollectionTache = em.merge(tacheCollectionTache);
            }
            em.remove(categorie);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categorie> findCategorieEntities() {
        return findCategorieEntities(true, -1, -1);
    }

    public List<Categorie> findCategorieEntities(int maxResults, int firstResult) {
        return findCategorieEntities(false, maxResults, firstResult);
    }

    private List<Categorie> findCategorieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Categorie as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Categorie findCategorie(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categorie.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategorieCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Categorie as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
