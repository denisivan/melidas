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
import org.lasmedilas.siates.jpa.entidades.Referencia;

/**
 *
 * @author Franco
 */
public class ReferenciaJpaController implements Serializable {

    public ReferenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referencia referencia) throws PreexistingEntityException, Exception {
        if (referencia.getPersonaCollection() == null) {
            referencia.setPersonaCollection(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : referencia.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getIdPersona());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            referencia.setPersonaCollection(attachedPersonaCollection);
            em.persist(referencia);
            for (Persona personaCollectionPersona : referencia.getPersonaCollection()) {
                personaCollectionPersona.getReferenciaCollection().add(referencia);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReferencia(referencia.getIdReferencia()) != null) {
                throw new PreexistingEntityException("Referencia " + referencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referencia referencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Referencia persistentReferencia = em.find(Referencia.class, referencia.getIdReferencia());
            Collection<Persona> personaCollectionOld = persistentReferencia.getPersonaCollection();
            Collection<Persona> personaCollectionNew = referencia.getPersonaCollection();
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getIdPersona());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            referencia.setPersonaCollection(personaCollectionNew);
            referencia = em.merge(referencia);
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    personaCollectionOldPersona.getReferenciaCollection().remove(referencia);
                    personaCollectionOldPersona = em.merge(personaCollectionOldPersona);
                }
            }
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    personaCollectionNewPersona.getReferenciaCollection().add(referencia);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = referencia.getIdReferencia();
                if (findReferencia(id) == null) {
                    throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.");
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
            Referencia referencia;
            try {
                referencia = em.getReference(Referencia.class, id);
                referencia.getIdReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.", enfe);
            }
            Collection<Persona> personaCollection = referencia.getPersonaCollection();
            for (Persona personaCollectionPersona : personaCollection) {
                personaCollectionPersona.getReferenciaCollection().remove(referencia);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.remove(referencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Referencia> findReferenciaEntities() {
        return findReferenciaEntities(true, -1, -1);
    }

    public List<Referencia> findReferenciaEntities(int maxResults, int firstResult) {
        return findReferenciaEntities(false, maxResults, firstResult);
    }

    private List<Referencia> findReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referencia.class));
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

    public Referencia findReferencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Referencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referencia> rt = cq.from(Referencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
