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
import org.hibernate.Hibernate;
import timereportfx.TimeReportFx;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.ProjetEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class SimpleProjetService implements Serializable, ProjetService {

    public SimpleProjetService( EntityManagerFactory emf) {
        //this.utx = utx;
        this.emf = emf;
    }
 //   private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public SimpleProjetService() {
       emf = TimeReportFx.getEMF();
    }

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(ProjetEntity projet) throws PreexistingEntityException, Exception {
        if (projet.getTacheCollection() == null) {
            projet.setTacheCollection(new ArrayList<TacheEntity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TacheEntity> attachedTacheCollection = new ArrayList<TacheEntity>();
            for (TacheEntity tacheCollectionTacheToAttach : projet.getTacheCollection()) {
                tacheCollectionTacheToAttach = em.getReference(tacheCollectionTacheToAttach.getClass(), tacheCollectionTacheToAttach.getIdtache());
                attachedTacheCollection.add(tacheCollectionTacheToAttach);
            }
            projet.setTacheCollection(attachedTacheCollection);
            em.persist(projet);
            for (TacheEntity tacheCollectionTache : projet.getTacheCollection()) {
                ProjetEntity oldIdprojetOfTacheCollectionTache = tacheCollectionTache.getIdprojet();
                tacheCollectionTache.setIdprojet(projet);
                tacheCollectionTache = em.merge(tacheCollectionTache);
                if (oldIdprojetOfTacheCollectionTache != null) {
                    oldIdprojetOfTacheCollectionTache.getTacheCollection().remove(tacheCollectionTache);
                    oldIdprojetOfTacheCollectionTache = em.merge(oldIdprojetOfTacheCollectionTache);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProjet(projet.getIdprojet()) != null) {
                throw new PreexistingEntityException("Projet " + projet + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(ProjetEntity projet) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProjetEntity persistentProjet = em.find(ProjetEntity.class, projet.getIdprojet());
            Collection<TacheEntity> tacheCollectionOld = persistentProjet.getTacheCollection();
            Collection<TacheEntity> tacheCollectionNew = projet.getTacheCollection();
            Collection<TacheEntity> attachedTacheCollectionNew = new ArrayList<TacheEntity>();
            for (TacheEntity tacheCollectionNewTacheToAttach : tacheCollectionNew) {
                tacheCollectionNewTacheToAttach = em.getReference(tacheCollectionNewTacheToAttach.getClass(), tacheCollectionNewTacheToAttach.getIdtache());
                attachedTacheCollectionNew.add(tacheCollectionNewTacheToAttach);
            }
            tacheCollectionNew = attachedTacheCollectionNew;
            projet.setTacheCollection(tacheCollectionNew);
            projet = em.merge(projet);
            for (TacheEntity tacheCollectionOldTache : tacheCollectionOld) {
                if (!tacheCollectionNew.contains(tacheCollectionOldTache)) {
                    tacheCollectionOldTache.setIdprojet(null);
                    tacheCollectionOldTache = em.merge(tacheCollectionOldTache);
                }
            }
            for (TacheEntity tacheCollectionNewTache : tacheCollectionNew) {
                if (!tacheCollectionOld.contains(tacheCollectionNewTache)) {
                    ProjetEntity oldIdprojetOfTacheCollectionNewTache = tacheCollectionNewTache.getIdprojet();
                    tacheCollectionNewTache.setIdprojet(projet);
                    tacheCollectionNewTache = em.merge(tacheCollectionNewTache);
                    if (oldIdprojetOfTacheCollectionNewTache != null && !oldIdprojetOfTacheCollectionNewTache.equals(projet)) {
                        oldIdprojetOfTacheCollectionNewTache.getTacheCollection().remove(tacheCollectionNewTache);
                        oldIdprojetOfTacheCollectionNewTache = em.merge(oldIdprojetOfTacheCollectionNewTache);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = projet.getIdprojet();
                if (findProjet(id) == null) {
                    throw new NonexistentEntityException("The projet with id " + id + " no longer exists.");
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
            ProjetEntity projet;
            try {
                projet = em.getReference(ProjetEntity.class, id);
                projet.getIdprojet();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projet with id " + id + " no longer exists.", enfe);
            }
            Collection<TacheEntity> tacheCollection = projet.getTacheCollection();
            for (TacheEntity tacheCollectionTache : tacheCollection) {
                tacheCollectionTache.setIdprojet(null);
                tacheCollectionTache = em.merge(tacheCollectionTache);
            }
            em.remove(projet);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<ProjetEntity> findProjetEntities() {
        return findProjetEntities(true, -1, -1);
    }

    public List<ProjetEntity> findProjetEntities(int maxResults, int firstResult) {
        return findProjetEntities(false, maxResults, firstResult);
    }

    private List<ProjetEntity> findProjetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
      //  em.getTransaction().begin();
        List<ProjetEntity> lst;
        try {
            Query q = em.createQuery("select object(o) from ProjetEntity as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            lst = q.getResultList();
            lst.size();
            return lst;
        } finally {
           // em.getTransaction().commit();
            //em.close();
        }
    }

    @Override
    public ProjetEntity findProjet(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjetEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjetCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from ProjetEntity as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
