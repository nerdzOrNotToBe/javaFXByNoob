/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.models.entities.TacheEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.CategorieEntity;

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

    public void create(CategorieEntity categorie) throws PreexistingEntityException, Exception {
        if (categorie.getTacheCollection() == null) {
            categorie.setTacheCollection(new ArrayList<TacheEntity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TacheEntity> attachedTacheCollection = new ArrayList<TacheEntity>();
            for (TacheEntity tacheCollectionTacheToAttach : categorie.getTacheCollection()) {
                tacheCollectionTacheToAttach = em.getReference(tacheCollectionTacheToAttach.getClass(), tacheCollectionTacheToAttach.getIdtache());
                attachedTacheCollection.add(tacheCollectionTacheToAttach);
            }
            categorie.setTacheCollection(attachedTacheCollection);
            em.persist(categorie);
            for (TacheEntity tacheCollectionTache : categorie.getTacheCollection()) {
                CategorieEntity oldCdCtgOfTacheCollectionTache = tacheCollectionTache.getCdCtg();
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

    public void edit(CategorieEntity categorie) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategorieEntity persistentCategorie = em.find(CategorieEntity.class, categorie.getCdCtg());
            Collection<TacheEntity> tacheCollectionOld = persistentCategorie.getTacheCollection();
            Collection<TacheEntity> tacheCollectionNew = categorie.getTacheCollection();
            Collection<TacheEntity> attachedTacheCollectionNew = new ArrayList<TacheEntity>();
            for (TacheEntity tacheCollectionNewTacheToAttach : tacheCollectionNew) {
                tacheCollectionNewTacheToAttach = em.getReference(tacheCollectionNewTacheToAttach.getClass(), tacheCollectionNewTacheToAttach.getIdtache());
                attachedTacheCollectionNew.add(tacheCollectionNewTacheToAttach);
            }
            tacheCollectionNew = attachedTacheCollectionNew;
            categorie.setTacheCollection(tacheCollectionNew);
            categorie = em.merge(categorie);
            for (TacheEntity tacheCollectionOldTache : tacheCollectionOld) {
                if (!tacheCollectionNew.contains(tacheCollectionOldTache)) {
                    tacheCollectionOldTache.setCdCtg(null);
                    tacheCollectionOldTache = em.merge(tacheCollectionOldTache);
                }
            }
            for (TacheEntity tacheCollectionNewTache : tacheCollectionNew) {
                if (!tacheCollectionOld.contains(tacheCollectionNewTache)) {
                    CategorieEntity oldCdCtgOfTacheCollectionNewTache = tacheCollectionNewTache.getCdCtg();
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
            CategorieEntity categorie;
            try {
                categorie = em.getReference(CategorieEntity.class, id);
                categorie.getCdCtg();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorie with id " + id + " no longer exists.", enfe);
            }
            Collection<TacheEntity> tacheCollection = categorie.getTacheCollection();
            for (TacheEntity tacheCollectionTache : tacheCollection) {
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

    public List<CategorieEntity> findCategorieEntities() {
        return findCategorieEntities(true, -1, -1);
    }

    public List<CategorieEntity> findCategorieEntities(int maxResults, int firstResult) {
        return findCategorieEntities(false, maxResults, firstResult);
    }

    private List<CategorieEntity> findCategorieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from CategorieEntity as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CategorieEntity findCategorie(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategorieEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategorieCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from CategorieEntity as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
