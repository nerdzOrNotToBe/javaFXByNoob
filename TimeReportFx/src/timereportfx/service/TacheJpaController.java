/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import timereportfx.models.entities.Tache;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import timereportfx.models.entities.Utilisateur;
import timereportfx.models.entities.Projet;
import timereportfx.models.entities.Categorie;
import timereportfx.models.entities.Timereport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.transaction.UserTransaction;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;

/**
 *
 * @author Dimitri Lebel
 */
public class TacheJpaController implements Serializable {

    public TacheJpaController( EntityManagerFactory emf) {
     //   this.utx = utx;
        this.emf = emf;
    }
 //   private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tache tache) throws PreexistingEntityException, Exception {
        if (tache.getTimereportCollection() == null) {
            tache.setTimereportCollection(new ArrayList<Timereport>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilisateur idutilisateur = tache.getIdutilisateur();
            if (idutilisateur != null) {
                idutilisateur = em.getReference(idutilisateur.getClass(), idutilisateur.getIdutilisateur());
                tache.setIdutilisateur(idutilisateur);
            }
            Projet idprojet = tache.getIdprojet();
            if (idprojet != null) {
                idprojet = em.getReference(idprojet.getClass(), idprojet.getIdprojet());
                tache.setIdprojet(idprojet);
            }
            Categorie cdCtg = tache.getCdCtg();
            if (cdCtg != null) {
                cdCtg = em.getReference(cdCtg.getClass(), cdCtg.getCdCtg());
                tache.setCdCtg(cdCtg);
            }
            Collection<Timereport> attachedTimereportCollection = new ArrayList<Timereport>();
            for (Timereport timereportCollectionTimereportToAttach : tache.getTimereportCollection()) {
                timereportCollectionTimereportToAttach = em.getReference(timereportCollectionTimereportToAttach.getClass(), timereportCollectionTimereportToAttach.getIdtimereport());
                attachedTimereportCollection.add(timereportCollectionTimereportToAttach);
            }
            tache.setTimereportCollection(attachedTimereportCollection);
            em.persist(tache);
            if (idutilisateur != null) {
                idutilisateur.getTacheCollection().add(tache);
                idutilisateur = em.merge(idutilisateur);
            }
            if (idprojet != null) {
                idprojet.getTacheCollection().add(tache);
                idprojet = em.merge(idprojet);
            }
            if (cdCtg != null) {
                cdCtg.getTacheCollection().add(tache);
                cdCtg = em.merge(cdCtg);
            }
            for (Timereport timereportCollectionTimereport : tache.getTimereportCollection()) {
                Tache oldIdtacheOfTimereportCollectionTimereport = timereportCollectionTimereport.getIdtache();
                timereportCollectionTimereport.setIdtache(tache);
                timereportCollectionTimereport = em.merge(timereportCollectionTimereport);
                if (oldIdtacheOfTimereportCollectionTimereport != null) {
                    oldIdtacheOfTimereportCollectionTimereport.getTimereportCollection().remove(timereportCollectionTimereport);
                    oldIdtacheOfTimereportCollectionTimereport = em.merge(oldIdtacheOfTimereportCollectionTimereport);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTache(tache.getIdtache()) != null) {
                throw new PreexistingEntityException("Tache " + tache + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tache tache) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tache persistentTache = em.find(Tache.class, tache.getIdtache());
            Utilisateur idutilisateurOld = persistentTache.getIdutilisateur();
            Utilisateur idutilisateurNew = tache.getIdutilisateur();
            Projet idprojetOld = persistentTache.getIdprojet();
            Projet idprojetNew = tache.getIdprojet();
            Categorie cdCtgOld = persistentTache.getCdCtg();
            Categorie cdCtgNew = tache.getCdCtg();
            Collection<Timereport> timereportCollectionOld = persistentTache.getTimereportCollection();
            Collection<Timereport> timereportCollectionNew = tache.getTimereportCollection();
            if (idutilisateurNew != null) {
                idutilisateurNew = em.getReference(idutilisateurNew.getClass(), idutilisateurNew.getIdutilisateur());
                tache.setIdutilisateur(idutilisateurNew);
            }
            if (idprojetNew != null) {
                idprojetNew = em.getReference(idprojetNew.getClass(), idprojetNew.getIdprojet());
                tache.setIdprojet(idprojetNew);
            }
            if (cdCtgNew != null) {
                cdCtgNew = em.getReference(cdCtgNew.getClass(), cdCtgNew.getCdCtg());
                tache.setCdCtg(cdCtgNew);
            }
            Collection<Timereport> attachedTimereportCollectionNew = new ArrayList<Timereport>();
            for (Timereport timereportCollectionNewTimereportToAttach : timereportCollectionNew) {
                timereportCollectionNewTimereportToAttach = em.getReference(timereportCollectionNewTimereportToAttach.getClass(), timereportCollectionNewTimereportToAttach.getIdtimereport());
                attachedTimereportCollectionNew.add(timereportCollectionNewTimereportToAttach);
            }
            timereportCollectionNew = attachedTimereportCollectionNew;
            tache.setTimereportCollection(timereportCollectionNew);
            tache = em.merge(tache);
            if (idutilisateurOld != null && !idutilisateurOld.equals(idutilisateurNew)) {
                idutilisateurOld.getTacheCollection().remove(tache);
                idutilisateurOld = em.merge(idutilisateurOld);
            }
            if (idutilisateurNew != null && !idutilisateurNew.equals(idutilisateurOld)) {
                idutilisateurNew.getTacheCollection().add(tache);
                idutilisateurNew = em.merge(idutilisateurNew);
            }
            if (idprojetOld != null && !idprojetOld.equals(idprojetNew)) {
                idprojetOld.getTacheCollection().remove(tache);
                idprojetOld = em.merge(idprojetOld);
            }
            if (idprojetNew != null && !idprojetNew.equals(idprojetOld)) {
                idprojetNew.getTacheCollection().add(tache);
                idprojetNew = em.merge(idprojetNew);
            }
            if (cdCtgOld != null && !cdCtgOld.equals(cdCtgNew)) {
                cdCtgOld.getTacheCollection().remove(tache);
                cdCtgOld = em.merge(cdCtgOld);
            }
            if (cdCtgNew != null && !cdCtgNew.equals(cdCtgOld)) {
                cdCtgNew.getTacheCollection().add(tache);
                cdCtgNew = em.merge(cdCtgNew);
            }
            for (Timereport timereportCollectionOldTimereport : timereportCollectionOld) {
                if (!timereportCollectionNew.contains(timereportCollectionOldTimereport)) {
                    timereportCollectionOldTimereport.setIdtache(null);
                    timereportCollectionOldTimereport = em.merge(timereportCollectionOldTimereport);
                }
            }
            for (Timereport timereportCollectionNewTimereport : timereportCollectionNew) {
                if (!timereportCollectionOld.contains(timereportCollectionNewTimereport)) {
                    Tache oldIdtacheOfTimereportCollectionNewTimereport = timereportCollectionNewTimereport.getIdtache();
                    timereportCollectionNewTimereport.setIdtache(tache);
                    timereportCollectionNewTimereport = em.merge(timereportCollectionNewTimereport);
                    if (oldIdtacheOfTimereportCollectionNewTimereport != null && !oldIdtacheOfTimereportCollectionNewTimereport.equals(tache)) {
                        oldIdtacheOfTimereportCollectionNewTimereport.getTimereportCollection().remove(timereportCollectionNewTimereport);
                        oldIdtacheOfTimereportCollectionNewTimereport = em.merge(oldIdtacheOfTimereportCollectionNewTimereport);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tache.getIdtache();
                if (findTache(id) == null) {
                    throw new NonexistentEntityException("The tache with id " + id + " no longer exists.");
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
            Tache tache;
            try {
                tache = em.getReference(Tache.class, id);
                tache.getIdtache();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tache with id " + id + " no longer exists.", enfe);
            }
            Utilisateur idutilisateur = tache.getIdutilisateur();
            if (idutilisateur != null) {
                idutilisateur.getTacheCollection().remove(tache);
                idutilisateur = em.merge(idutilisateur);
            }
            Projet idprojet = tache.getIdprojet();
            if (idprojet != null) {
                idprojet.getTacheCollection().remove(tache);
                idprojet = em.merge(idprojet);
            }
            Categorie cdCtg = tache.getCdCtg();
            if (cdCtg != null) {
                cdCtg.getTacheCollection().remove(tache);
                cdCtg = em.merge(cdCtg);
            }
            Collection<Timereport> timereportCollection = tache.getTimereportCollection();
            for (Timereport timereportCollectionTimereport : timereportCollection) {
                timereportCollectionTimereport.setIdtache(null);
                timereportCollectionTimereport = em.merge(timereportCollectionTimereport);
            }
            em.remove(tache);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tache> findTacheEntities() {
        return findTacheEntities(true, -1, -1);
    }

    public List<Tache> findTacheEntities(int maxResults, int firstResult) {
        return findTacheEntities(false, maxResults, firstResult);
    }

    private List<Tache> findTacheEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Tache as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tache findTache(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tache.class, id);
        } finally {
            em.close();
        }
    }

    public int getTacheCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Tache as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
