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
import org.lasmedilas.siates.jpa.entidades.Asesoria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.IllegalOrphanException;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.Modalidad;

/**
 *
 * @author Franco
 */
public class ModalidadJpaController implements Serializable {

    public ModalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modalidad modalidad) throws PreexistingEntityException, Exception {
        if (modalidad.getAsesoriaCollection() == null) {
            modalidad.setAsesoriaCollection(new ArrayList<Asesoria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Asesoria> attachedAsesoriaCollection = new ArrayList<Asesoria>();
            for (Asesoria asesoriaCollectionAsesoriaToAttach : modalidad.getAsesoriaCollection()) {
                asesoriaCollectionAsesoriaToAttach = em.getReference(asesoriaCollectionAsesoriaToAttach.getClass(), asesoriaCollectionAsesoriaToAttach.getAsesoriaPK());
                attachedAsesoriaCollection.add(asesoriaCollectionAsesoriaToAttach);
            }
            modalidad.setAsesoriaCollection(attachedAsesoriaCollection);
            em.persist(modalidad);
            for (Asesoria asesoriaCollectionAsesoria : modalidad.getAsesoriaCollection()) {
                Modalidad oldModalidadOfAsesoriaCollectionAsesoria = asesoriaCollectionAsesoria.getModalidad();
                asesoriaCollectionAsesoria.setModalidad(modalidad);
                asesoriaCollectionAsesoria = em.merge(asesoriaCollectionAsesoria);
                if (oldModalidadOfAsesoriaCollectionAsesoria != null) {
                    oldModalidadOfAsesoriaCollectionAsesoria.getAsesoriaCollection().remove(asesoriaCollectionAsesoria);
                    oldModalidadOfAsesoriaCollectionAsesoria = em.merge(oldModalidadOfAsesoriaCollectionAsesoria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findModalidad(modalidad.getIdModalidad()) != null) {
                throw new PreexistingEntityException("Modalidad " + modalidad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Modalidad modalidad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modalidad persistentModalidad = em.find(Modalidad.class, modalidad.getIdModalidad());
            Collection<Asesoria> asesoriaCollectionOld = persistentModalidad.getAsesoriaCollection();
            Collection<Asesoria> asesoriaCollectionNew = modalidad.getAsesoriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Asesoria asesoriaCollectionOldAsesoria : asesoriaCollectionOld) {
                if (!asesoriaCollectionNew.contains(asesoriaCollectionOldAsesoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asesoria " + asesoriaCollectionOldAsesoria + " since its modalidad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Asesoria> attachedAsesoriaCollectionNew = new ArrayList<Asesoria>();
            for (Asesoria asesoriaCollectionNewAsesoriaToAttach : asesoriaCollectionNew) {
                asesoriaCollectionNewAsesoriaToAttach = em.getReference(asesoriaCollectionNewAsesoriaToAttach.getClass(), asesoriaCollectionNewAsesoriaToAttach.getAsesoriaPK());
                attachedAsesoriaCollectionNew.add(asesoriaCollectionNewAsesoriaToAttach);
            }
            asesoriaCollectionNew = attachedAsesoriaCollectionNew;
            modalidad.setAsesoriaCollection(asesoriaCollectionNew);
            modalidad = em.merge(modalidad);
            for (Asesoria asesoriaCollectionNewAsesoria : asesoriaCollectionNew) {
                if (!asesoriaCollectionOld.contains(asesoriaCollectionNewAsesoria)) {
                    Modalidad oldModalidadOfAsesoriaCollectionNewAsesoria = asesoriaCollectionNewAsesoria.getModalidad();
                    asesoriaCollectionNewAsesoria.setModalidad(modalidad);
                    asesoriaCollectionNewAsesoria = em.merge(asesoriaCollectionNewAsesoria);
                    if (oldModalidadOfAsesoriaCollectionNewAsesoria != null && !oldModalidadOfAsesoriaCollectionNewAsesoria.equals(modalidad)) {
                        oldModalidadOfAsesoriaCollectionNewAsesoria.getAsesoriaCollection().remove(asesoriaCollectionNewAsesoria);
                        oldModalidadOfAsesoriaCollectionNewAsesoria = em.merge(oldModalidadOfAsesoriaCollectionNewAsesoria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = modalidad.getIdModalidad();
                if (findModalidad(id) == null) {
                    throw new NonexistentEntityException("The modalidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modalidad modalidad;
            try {
                modalidad = em.getReference(Modalidad.class, id);
                modalidad.getIdModalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modalidad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Asesoria> asesoriaCollectionOrphanCheck = modalidad.getAsesoriaCollection();
            for (Asesoria asesoriaCollectionOrphanCheckAsesoria : asesoriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Modalidad (" + modalidad + ") cannot be destroyed since the Asesoria " + asesoriaCollectionOrphanCheckAsesoria + " in its asesoriaCollection field has a non-nullable modalidad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(modalidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Modalidad> findModalidadEntities() {
        return findModalidadEntities(true, -1, -1);
    }

    public List<Modalidad> findModalidadEntities(int maxResults, int firstResult) {
        return findModalidadEntities(false, maxResults, firstResult);
    }

    private List<Modalidad> findModalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modalidad.class));
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

    public Modalidad findModalidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modalidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getModalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modalidad> rt = cq.from(Modalidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
