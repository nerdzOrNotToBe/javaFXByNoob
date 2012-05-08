/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.util.List;
import javax.persistence.EntityManager;
import timereportfx.models.entities.ProjetEntity;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;

/**
 *
 * @author dimi
 */
public interface ProjetService {

    void create(ProjetEntity projet) throws PreexistingEntityException, Exception;

    void destroy(Integer id) throws NonexistentEntityException;

    void edit(ProjetEntity projet) throws NonexistentEntityException, Exception;

    ProjetEntity findProjet(Integer id);

    List<ProjetEntity> findProjetEntities();

    EntityManager getEntityManager();
    
}
