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
import org.lasmedilas.siates.jpa.entidades.AtencionEspecializada;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.Institucion;

/**
 *
 * @author Franco
 */
public class InstitucionJpaController implements Serializable {

    public InstitucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Institucion institucion) throws PreexistingEntityException, Exception {
        if (institucion.getAtencionEspecializadaCollection() == null) {
            institucion.setAtencionEspecializadaCollection(new ArrayList<AtencionEspecializada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollection = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializadaToAttach : institucion.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollection.add(atencionEspecializadaCollectionAtencionEspecializadaToAttach);
            }
            institucion.setAtencionEspecializadaCollection(attachedAtencionEspecializadaCollection);
            em.persist(institucion);
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : institucion.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializada.getInstitucionCollection().add(institucion);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInstitucion(institucion.getIdInstitucion()) != null) {
                throw new PreexistingEntityException("Institucion " + institucion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Institucion institucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Institucion persistentInstitucion = em.find(Institucion.class, institucion.getIdInstitucion());
            Collection<AtencionEspecializada> atencionEspecializadaCollectionOld = persistentInstitucion.getAtencionEspecializadaCollection();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionNew = institucion.getAtencionEspecializadaCollection();
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollectionNew = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializadaToAttach : atencionEspecializadaCollectionNew) {
                atencionEspecializadaCollectionNewAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollectionNew.add(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach);
            }
            atencionEspecializadaCollectionNew = attachedAtencionEspecializadaCollectionNew;
            institucion.setAtencionEspecializadaCollection(atencionEspecializadaCollectionNew);
            institucion = em.merge(institucion);
            for (AtencionEspecializada atencionEspecializadaCollectionOldAtencionEspecializada : atencionEspecializadaCollectionOld) {
                if (!atencionEspecializadaCollectionNew.contains(atencionEspecializadaCollectionOldAtencionEspecializada)) {
                    atencionEspecializadaCollectionOldAtencionEspecializada.getInstitucionCollection().remove(institucion);
                    atencionEspecializadaCollectionOldAtencionEspecializada = em.merge(atencionEspecializadaCollectionOldAtencionEspecializada);
                }
            }
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializada : atencionEspecializadaCollectionNew) {
                if (!atencionEspecializadaCollectionOld.contains(atencionEspecializadaCollectionNewAtencionEspecializada)) {
                    atencionEspecializadaCollectionNewAtencionEspecializada.getInstitucionCollection().add(institucion);
                    atencionEspecializadaCollectionNewAtencionEspecializada = em.merge(atencionEspecializadaCollectionNewAtencionEspecializada);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = institucion.getIdInstitucion();
                if (findInstitucion(id) == null) {
                    throw new NonexistentEntityException("The institucion with id " + id + " no longer exists.");
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
            Institucion institucion;
            try {
                institucion = em.getReference(Institucion.class, id);
                institucion.getIdInstitucion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The institucion with id " + id + " no longer exists.", enfe);
            }
            Collection<AtencionEspecializada> atencionEspecializadaCollection = institucion.getAtencionEspecializadaCollection();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : atencionEspecializadaCollection) {
                atencionEspecializadaCollectionAtencionEspecializada.getInstitucionCollection().remove(institucion);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
            }
            em.remove(institucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Institucion> findInstitucionEntities() {
        return findInstitucionEntities(true, -1, -1);
    }

    public List<Institucion> findInstitucionEntities(int maxResults, int firstResult) {
        return findInstitucionEntities(false, maxResults, firstResult);
    }

    private List<Institucion> findInstitucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Institucion.class));
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

    public Institucion findInstitucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Institucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstitucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Institucion> rt = cq.from(Institucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
