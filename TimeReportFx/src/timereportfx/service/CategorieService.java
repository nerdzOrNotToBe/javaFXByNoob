/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timereportfx.service;

import java.util.List;
import javax.persistence.EntityManager;
import timereportfx.models.entities.CategorieEntity;
import timereportfx.service.exceptions.NonexistentEntityException;
import timereportfx.service.exceptions.PreexistingEntityException;

/**
 *
 * @author dimi
 */
public interface CategorieService {

    void create(CategorieEntity categorie) throws PreexistingEntityException, Exception;

    void destroy(String id) throws NonexistentEntityException;

    void edit(CategorieEntity categorie) throws NonexistentEntityException, Exception;

    CategorieEntity findCategorie(String id);

    List<CategorieEntity> findCategorieEntities();

    EntityManager getEntityManager();
    
}
