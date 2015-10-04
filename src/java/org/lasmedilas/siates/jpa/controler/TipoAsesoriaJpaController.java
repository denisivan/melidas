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
import org.lasmedilas.siates.jpa.entidades.TipoAsesoria;

/**
 *
 * @author Franco
 */
public class TipoAsesoriaJpaController implements Serializable {

    public TipoAsesoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoAsesoria tipoAsesoria) throws PreexistingEntityException, Exception {
        if (tipoAsesoria.getAsesoriaCollection() == null) {
            tipoAsesoria.setAsesoriaCollection(new ArrayList<Asesoria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Asesoria> attachedAsesoriaCollection = new ArrayList<Asesoria>();
            for (Asesoria asesoriaCollectionAsesoriaToAttach : tipoAsesoria.getAsesoriaCollection()) {
                asesoriaCollectionAsesoriaToAttach = em.getReference(asesoriaCollectionAsesoriaToAttach.getClass(), asesoriaCollectionAsesoriaToAttach.getAsesoriaPK());
                attachedAsesoriaCollection.add(asesoriaCollectionAsesoriaToAttach);
            }
            tipoAsesoria.setAsesoriaCollection(attachedAsesoriaCollection);
            em.persist(tipoAsesoria);
            for (Asesoria asesoriaCollectionAsesoria : tipoAsesoria.getAsesoriaCollection()) {
                TipoAsesoria oldTipoAsesoriaOfAsesoriaCollectionAsesoria = asesoriaCollectionAsesoria.getTipoAsesoria();
                asesoriaCollectionAsesoria.setTipoAsesoria(tipoAsesoria);
                asesoriaCollectionAsesoria = em.merge(asesoriaCollectionAsesoria);
                if (oldTipoAsesoriaOfAsesoriaCollectionAsesoria != null) {
                    oldTipoAsesoriaOfAsesoriaCollectionAsesoria.getAsesoriaCollection().remove(asesoriaCollectionAsesoria);
                    oldTipoAsesoriaOfAsesoriaCollectionAsesoria = em.merge(oldTipoAsesoriaOfAsesoriaCollectionAsesoria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoAsesoria(tipoAsesoria.getIdTipoAsesoria()) != null) {
                throw new PreexistingEntityException("TipoAsesoria " + tipoAsesoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAsesoria tipoAsesoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoAsesoria persistentTipoAsesoria = em.find(TipoAsesoria.class, tipoAsesoria.getIdTipoAsesoria());
            Collection<Asesoria> asesoriaCollectionOld = persistentTipoAsesoria.getAsesoriaCollection();
            Collection<Asesoria> asesoriaCollectionNew = tipoAsesoria.getAsesoriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Asesoria asesoriaCollectionOldAsesoria : asesoriaCollectionOld) {
                if (!asesoriaCollectionNew.contains(asesoriaCollectionOldAsesoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asesoria " + asesoriaCollectionOldAsesoria + " since its tipoAsesoria field is not nullable.");
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
            tipoAsesoria.setAsesoriaCollection(asesoriaCollectionNew);
            tipoAsesoria = em.merge(tipoAsesoria);
            for (Asesoria asesoriaCollectionNewAsesoria : asesoriaCollectionNew) {
                if (!asesoriaCollectionOld.contains(asesoriaCollectionNewAsesoria)) {
                    TipoAsesoria oldTipoAsesoriaOfAsesoriaCollectionNewAsesoria = asesoriaCollectionNewAsesoria.getTipoAsesoria();
                    asesoriaCollectionNewAsesoria.setTipoAsesoria(tipoAsesoria);
                    asesoriaCollectionNewAsesoria = em.merge(asesoriaCollectionNewAsesoria);
                    if (oldTipoAsesoriaOfAsesoriaCollectionNewAsesoria != null && !oldTipoAsesoriaOfAsesoriaCollectionNewAsesoria.equals(tipoAsesoria)) {
                        oldTipoAsesoriaOfAsesoriaCollectionNewAsesoria.getAsesoriaCollection().remove(asesoriaCollectionNewAsesoria);
                        oldTipoAsesoriaOfAsesoriaCollectionNewAsesoria = em.merge(oldTipoAsesoriaOfAsesoriaCollectionNewAsesoria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoAsesoria.getIdTipoAsesoria();
                if (findTipoAsesoria(id) == null) {
                    throw new NonexistentEntityException("The tipoAsesoria with id " + id + " no longer exists.");
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
            TipoAsesoria tipoAsesoria;
            try {
                tipoAsesoria = em.getReference(TipoAsesoria.class, id);
                tipoAsesoria.getIdTipoAsesoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAsesoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Asesoria> asesoriaCollectionOrphanCheck = tipoAsesoria.getAsesoriaCollection();
            for (Asesoria asesoriaCollectionOrphanCheckAsesoria : asesoriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoAsesoria (" + tipoAsesoria + ") cannot be destroyed since the Asesoria " + asesoriaCollectionOrphanCheckAsesoria + " in its asesoriaCollection field has a non-nullable tipoAsesoria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoAsesoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoAsesoria> findTipoAsesoriaEntities() {
        return findTipoAsesoriaEntities(true, -1, -1);
    }

    public List<TipoAsesoria> findTipoAsesoriaEntities(int maxResults, int firstResult) {
        return findTipoAsesoriaEntities(false, maxResults, firstResult);
    }

    private List<TipoAsesoria> findTipoAsesoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAsesoria.class));
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

    public TipoAsesoria findTipoAsesoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoAsesoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAsesoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAsesoria> rt = cq.from(TipoAsesoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
