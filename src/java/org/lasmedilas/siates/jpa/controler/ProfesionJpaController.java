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
import org.lasmedilas.siates.jpa.entidades.Profesion;

/**
 *
 * @author Franco
 */
public class ProfesionJpaController implements Serializable {

    public ProfesionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesion profesion) throws PreexistingEntityException, Exception {
        if (profesion.getPersonaCollection() == null) {
            profesion.setPersonaCollection(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : profesion.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getIdPersona());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            profesion.setPersonaCollection(attachedPersonaCollection);
            em.persist(profesion);
            for (Persona personaCollectionPersona : profesion.getPersonaCollection()) {
                personaCollectionPersona.getProfesionCollection().add(profesion);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfesion(profesion.getIdProfesion()) != null) {
                throw new PreexistingEntityException("Profesion " + profesion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesion profesion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesion persistentProfesion = em.find(Profesion.class, profesion.getIdProfesion());
            Collection<Persona> personaCollectionOld = persistentProfesion.getPersonaCollection();
            Collection<Persona> personaCollectionNew = profesion.getPersonaCollection();
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getIdPersona());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            profesion.setPersonaCollection(personaCollectionNew);
            profesion = em.merge(profesion);
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    personaCollectionOldPersona.getProfesionCollection().remove(profesion);
                    personaCollectionOldPersona = em.merge(personaCollectionOldPersona);
                }
            }
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    personaCollectionNewPersona.getProfesionCollection().add(profesion);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profesion.getIdProfesion();
                if (findProfesion(id) == null) {
                    throw new NonexistentEntityException("The profesion with id " + id + " no longer exists.");
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
            Profesion profesion;
            try {
                profesion = em.getReference(Profesion.class, id);
                profesion.getIdProfesion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesion with id " + id + " no longer exists.", enfe);
            }
            Collection<Persona> personaCollection = profesion.getPersonaCollection();
            for (Persona personaCollectionPersona : personaCollection) {
                personaCollectionPersona.getProfesionCollection().remove(profesion);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.remove(profesion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesion> findProfesionEntities() {
        return findProfesionEntities(true, -1, -1);
    }

    public List<Profesion> findProfesionEntities(int maxResults, int firstResult) {
        return findProfesionEntities(false, maxResults, firstResult);
    }

    private List<Profesion> findProfesionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesion.class));
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

    public Profesion findProfesion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesion.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesion> rt = cq.from(Profesion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
