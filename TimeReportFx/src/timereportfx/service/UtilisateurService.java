/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import timereportfx.models.entities.UtilisateurEntity;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;

/**
 *
 * @author dimi
 */
public interface UtilisateurService extends Serializable {

    void create(UtilisateurEntity utilisateur) throws PreexistingEntityException, Exception;

    void destroy(Integer id) throws NonexistentEntityException;

    void edit(UtilisateurEntity utilisateur) throws NonexistentEntityException, Exception;

    UtilisateurEntity findUtilisateur(Integer id);

    UtilisateurEntity findUtilisateurByNom(String nom);

    List<UtilisateurEntity> findUtilisateurEntities();

    EntityManager getEntityManager();

    boolean isOk(String nom);
    
}
