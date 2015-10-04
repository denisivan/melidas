/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.controler;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.lasmedilas.siates.jpa.entidades.Persona;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.TipoAgresor;

/**
 *
 * @author Franco
 */
public class TipoAgresorJpaController implements Serializable {

    public TipoAgresorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoAgresor tipoAgresor) throws PreexistingEntityException, Exception {
        if (tipoAgresor.getPersonaCollection() == null) {
            tipoAgresor.setPersonaCollection(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : tipoAgresor.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getIdPersona());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            tipoAgresor.setPersonaCollection(attachedPersonaCollection);
            em.persist(tipoAgresor);
            for (Persona personaCollectionPersona : tipoAgresor.getPersonaCollection()) {
                personaCollectionPersona.getTipoAgresorCollection().add(tipoAgresor);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoAgresor(tipoAgresor.getIdTipoAgresor()) != null) {
                throw new PreexistingEntityException("TipoAgresor " + tipoAgresor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAgresor tipoAgresor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoAgresor persistentTipoAgresor = em.find(TipoAgresor.class, tipoAgresor.getIdTipoAgresor());
            Collection<Persona> personaCollectionOld = persistentTipoAgresor.getPersonaCollection();
            Collection<Persona> personaCollectionNew = tipoAgresor.getPersonaCollection();
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getIdPersona());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            tipoAgresor.setPersonaCollection(personaCollectionNew);
            tipoAgresor = em.merge(tipoAgresor);
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    personaCollectionOldPersona.getTipoAgresorCollection().remove(tipoAgresor);
                    personaCollectionOldPersona = em.merge(personaCollectionOldPersona);
                }
            }
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    personaCollectionNewPersona.getTipoAgresorCollection().add(tipoAgresor);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoAgresor.getIdTipoAgresor();
                if (findTipoAgresor(id) == null) {
                    throw new NonexistentEntityException("The tipoAgresor with id " + id + " no longer exists.");
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
            TipoAgresor tipoAgresor;
            try {
                tipoAgresor = em.getReference(TipoAgresor.class, id);
                tipoAgresor.getIdTipoAgresor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAgresor with id " + id + " no longer exists.", enfe);
            }
            Collection<Persona> personaCollection = tipoAgresor.getPersonaCollection();
            for (Persona personaCollectionPersona : personaCollection) {
                personaCollectionPersona.getTipoAgresorCollection().remove(tipoAgresor);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.remove(tipoAgresor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoAgresor> findTipoAgresorEntities() {
        return findTipoAgresorEntities(true, -1, -1);
    }

    public List<TipoAgresor> findTipoAgresorEntities(int maxResults, int firstResult) {
        return findTipoAgresorEntities(false, maxResults, firstResult);
    }

    private List<TipoAgresor> findTipoAgresorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAgresor.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TipoAgresor findTipoAgresor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoAgresor.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAgresorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAgresor> rt = cq.from(TipoAgresor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
