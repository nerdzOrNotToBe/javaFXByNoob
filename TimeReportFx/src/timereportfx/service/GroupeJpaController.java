/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.models.entities.UtilisateurEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;
import timereportfx.models.entities.GroupeEntity;

/**
 *
 * @author Dimitri Lebel
 */
public class GroupeJpaController implements Serializable {

    public GroupeJpaController( EntityManagerFactory emf) {
       // this.utx = utx;
        this.emf = emf;
    }
  //  private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GroupeEntity groupe) throws PreexistingEntityException, Exception {
        if (groupe.getUtilisateurCollection() == null) {
            groupe.setUtilisateurCollection(new ArrayList<UtilisateurEntity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UtilisateurEntity> attachedUtilisateurCollection = new ArrayList<UtilisateurEntity>();
            for (UtilisateurEntity utilisateurCollectionUtilisateurToAttach : groupe.getUtilisateurCollection()) {
                utilisateurCollectionUtilisateurToAttach = em.getReference(utilisateurCollectionUtilisateurToAttach.getClass(), utilisateurCollectionUtilisateurToAttach.getIdutilisateur());
                attachedUtilisateurCollection.add(utilisateurCollectionUtilisateurToAttach);
            }
            groupe.setUtilisateurCollection(attachedUtilisateurCollection);
            em.persist(groupe);
            for (UtilisateurEntity utilisateurCollectionUtilisateur : groupe.getUtilisateurCollection()) {
                GroupeEntity oldIdgroupeOfUtilisateurCollectionUtilisateur = utilisateurCollectionUtilisateur.getIdgroupe();
                utilisateurCollectionUtilisateur.setIdgroupe(groupe);
                utilisateurCollectionUtilisateur = em.merge(utilisateurCollectionUtilisateur);
                if (oldIdgroupeOfUtilisateurCollectionUtilisateur != null) {
                    oldIdgroupeOfUtilisateurCollectionUtilisateur.getUtilisateurCollection().remove(utilisateurCollectionUtilisateur);
                    oldIdgroupeOfUtilisateurCollectionUtilisateur = em.merge(oldIdgroupeOfUtilisateurCollectionUtilisateur);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGroupe(groupe.getIdgroupe()) != null) {
                throw new PreexistingEntityException("Groupe " + groupe + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GroupeEntity groupe) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GroupeEntity persistentGroupe = em.find(GroupeEntity.class, groupe.getIdgroupe());
            Collection<UtilisateurEntity> utilisateurCollectionOld = persistentGroupe.getUtilisateurCollection();
            Collection<UtilisateurEntity> utilisateurCollectionNew = groupe.getUtilisateurCollection();
            Collection<UtilisateurEntity> attachedUtilisateurCollectionNew = new ArrayList<UtilisateurEntity>();
            for (UtilisateurEntity utilisateurCollectionNewUtilisateurToAttach : utilisateurCollectionNew) {
                utilisateurCollectionNewUtilisateurToAttach = em.getReference(utilisateurCollectionNewUtilisateurToAttach.getClass(), utilisateurCollectionNewUtilisateurToAttach.getIdutilisateur());
                attachedUtilisateurCollectionNew.add(utilisateurCollectionNewUtilisateurToAttach);
            }
            utilisateurCollectionNew = attachedUtilisateurCollectionNew;
            groupe.setUtilisateurCollection(utilisateurCollectionNew);
            groupe = em.merge(groupe);
            for (UtilisateurEntity utilisateurCollectionOldUtilisateur : utilisateurCollectionOld) {
                if (!utilisateurCollectionNew.contains(utilisateurCollectionOldUtilisateur)) {
                    utilisateurCollectionOldUtilisateur.setIdgroupe(null);
                    utilisateurCollectionOldUtilisateur = em.merge(utilisateurCollectionOldUtilisateur);
                }
            }
            for (UtilisateurEntity utilisateurCollectionNewUtilisateur : utilisateurCollectionNew) {
                if (!utilisateurCollectionOld.contains(utilisateurCollectionNewUtilisateur)) {
                    GroupeEntity oldIdgroupeOfUtilisateurCollectionNewUtilisateur = utilisateurCollectionNewUtilisateur.getIdgroupe();
                    utilisateurCollectionNewUtilisateur.setIdgroupe(groupe);
                    utilisateurCollectionNewUtilisateur = em.merge(utilisateurCollectionNewUtilisateur);
                    if (oldIdgroupeOfUtilisateurCollectionNewUtilisateur != null && !oldIdgroupeOfUtilisateurCollectionNewUtilisateur.equals(groupe)) {
                        oldIdgroupeOfUtilisateurCollectionNewUtilisateur.getUtilisateurCollection().remove(utilisateurCollectionNewUtilisateur);
                        oldIdgroupeOfUtilisateurCollectionNewUtilisateur = em.merge(oldIdgroupeOfUtilisateurCollectionNewUtilisateur);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = groupe.getIdgroupe();
                if (findGroupe(id) == null) {
                    throw new NonexistentEntityException("The groupe with id " + id + " no longer exists.");
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
            GroupeEntity groupe;
            try {
                groupe = em.getReference(GroupeEntity.class, id);
                groupe.getIdgroupe();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The groupe with id " + id + " no longer exists.", enfe);
            }
            Collection<UtilisateurEntity> utilisateurCollection = groupe.getUtilisateurCollection();
            for (UtilisateurEntity utilisateurCollectionUtilisateur : utilisateurCollection) {
                utilisateurCollectionUtilisateur.setIdgroupe(null);
                utilisateurCollectionUtilisateur = em.merge(utilisateurCollectionUtilisateur);
            }
            em.remove(groupe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GroupeEntity> findGroupeEntities() {
        return findGroupeEntities(true, -1, -1);
    }

    public List<GroupeEntity> findGroupeEntities(int maxResults, int firstResult) {
        return findGroupeEntities(false, maxResults, firstResult);
    }

    private List<GroupeEntity> findGroupeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from GroupeEntity as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public GroupeEntity findGroupe(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GroupeEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getGroupeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from GroupeEntity as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
