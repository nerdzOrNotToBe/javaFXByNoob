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
import org.hibernate.Hibernate;
import timereportfx.TimeReportFx;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.Projet;

/**
 *
 * @author Dimitri Lebel
 */
public class ProjetJpaController implements Serializable {

    public ProjetJpaController( EntityManagerFactory emf) {
        //this.utx = utx;
        this.emf = emf;
    }
 //   private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public ProjetJpaController() {
       emf = TimeReportFx.getEMF();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Projet projet) throws PreexistingEntityException, Exception {
        if (projet.getTacheCollection() == null) {
            projet.setTacheCollection(new ArrayList<Tache>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Tache> attachedTacheCollection = new ArrayList<Tache>();
            for (Tache tacheCollectionTacheToAttach : projet.getTacheCollection()) {
                tacheCollectionTacheToAttach = em.getReference(tacheCollectionTacheToAttach.getClass(), tacheCollectionTacheToAttach.getIdtache());
                attachedTacheCollection.add(tacheCollectionTacheToAttach);
            }
            projet.setTacheCollection(attachedTacheCollection);
            em.persist(projet);
            for (Tache tacheCollectionTache : projet.getTacheCollection()) {
                Projet oldIdprojetOfTacheCollectionTache = tacheCollectionTache.getIdprojet();
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

    public void edit(Projet projet) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Projet persistentProjet = em.find(Projet.class, projet.getIdprojet());
            Collection<Tache> tacheCollectionOld = persistentProjet.getTacheCollection();
            Collection<Tache> tacheCollectionNew = projet.getTacheCollection();
            Collection<Tache> attachedTacheCollectionNew = new ArrayList<Tache>();
            for (Tache tacheCollectionNewTacheToAttach : tacheCollectionNew) {
                tacheCollectionNewTacheToAttach = em.getReference(tacheCollectionNewTacheToAttach.getClass(), tacheCollectionNewTacheToAttach.getIdtache());
                attachedTacheCollectionNew.add(tacheCollectionNewTacheToAttach);
            }
            tacheCollectionNew = attachedTacheCollectionNew;
            projet.setTacheCollection(tacheCollectionNew);
            projet = em.merge(projet);
            for (Tache tacheCollectionOldTache : tacheCollectionOld) {
                if (!tacheCollectionNew.contains(tacheCollectionOldTache)) {
                    tacheCollectionOldTache.setIdprojet(null);
                    tacheCollectionOldTache = em.merge(tacheCollectionOldTache);
                }
            }
            for (Tache tacheCollectionNewTache : tacheCollectionNew) {
                if (!tacheCollectionOld.contains(tacheCollectionNewTache)) {
                    Projet oldIdprojetOfTacheCollectionNewTache = tacheCollectionNewTache.getIdprojet();
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Projet projet;
            try {
                projet = em.getReference(Projet.class, id);
                projet.getIdprojet();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projet with id " + id + " no longer exists.", enfe);
            }
            Collection<Tache> tacheCollection = projet.getTacheCollection();
            for (Tache tacheCollectionTache : tacheCollection) {
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

    public List<Projet> findProjetEntities() {
        return findProjetEntities(true, -1, -1);
    }

    public List<Projet> findProjetEntities(int maxResults, int firstResult) {
        return findProjetEntities(false, maxResults, firstResult);
    }

    private List<Projet> findProjetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
      //  em.getTransaction().begin();
        List<Projet> lst;
        try {
            Query q = em.createQuery("select object(o) from Projet as o");
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

    public Projet findProjet(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Projet.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjetCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Projet as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
