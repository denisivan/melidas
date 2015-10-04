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
import org.lasmedilas.siates.jpa.entidades.TipoViolencia;
import org.lasmedilas.siates.jpa.entidades.AtencionEspecializada;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.IllegalOrphanException;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;

/**
 *
 * @author Franco
 */
public class TipoViolenciaJpaController implements Serializable {

    public TipoViolenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoViolencia tipoViolencia) throws PreexistingEntityException, Exception {
        if (tipoViolencia.getAtencionEspecializadaCollection() == null) {
            tipoViolencia.setAtencionEspecializadaCollection(new ArrayList<AtencionEspecializada>());
        }
        if (tipoViolencia.getTipoViolenciaCollection() == null) {
            tipoViolencia.setTipoViolenciaCollection(new ArrayList<TipoViolencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoViolencia padreTipoViolencia = tipoViolencia.getPadreTipoViolencia();
            if (padreTipoViolencia != null) {
                padreTipoViolencia = em.getReference(padreTipoViolencia.getClass(), padreTipoViolencia.getIdTipoViolencia());
                tipoViolencia.setPadreTipoViolencia(padreTipoViolencia);
            }
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollection = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializadaToAttach : tipoViolencia.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollection.add(atencionEspecializadaCollectionAtencionEspecializadaToAttach);
            }
            tipoViolencia.setAtencionEspecializadaCollection(attachedAtencionEspecializadaCollection);
            Collection<TipoViolencia> attachedTipoViolenciaCollection = new ArrayList<TipoViolencia>();
            for (TipoViolencia tipoViolenciaCollectionTipoViolenciaToAttach : tipoViolencia.getTipoViolenciaCollection()) {
                tipoViolenciaCollectionTipoViolenciaToAttach = em.getReference(tipoViolenciaCollectionTipoViolenciaToAttach.getClass(), tipoViolenciaCollectionTipoViolenciaToAttach.getIdTipoViolencia());
                attachedTipoViolenciaCollection.add(tipoViolenciaCollectionTipoViolenciaToAttach);
            }
            tipoViolencia.setTipoViolenciaCollection(attachedTipoViolenciaCollection);
            em.persist(tipoViolencia);
            if (padreTipoViolencia != null) {
                padreTipoViolencia.getTipoViolenciaCollection().add(tipoViolencia);
                padreTipoViolencia = em.merge(padreTipoViolencia);
            }
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : tipoViolencia.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializada.getTipoViolenciaCollection().add(tipoViolencia);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
            }
            for (TipoViolencia tipoViolenciaCollectionTipoViolencia : tipoViolencia.getTipoViolenciaCollection()) {
                TipoViolencia oldPadreTipoViolenciaOfTipoViolenciaCollectionTipoViolencia = tipoViolenciaCollectionTipoViolencia.getPadreTipoViolencia();
                tipoViolenciaCollectionTipoViolencia.setPadreTipoViolencia(tipoViolencia);
                tipoViolenciaCollectionTipoViolencia = em.merge(tipoViolenciaCollectionTipoViolencia);
                if (oldPadreTipoViolenciaOfTipoViolenciaCollectionTipoViolencia != null) {
                    oldPadreTipoViolenciaOfTipoViolenciaCollectionTipoViolencia.getTipoViolenciaCollection().remove(tipoViolenciaCollectionTipoViolencia);
                    oldPadreTipoViolenciaOfTipoViolenciaCollectionTipoViolencia = em.merge(oldPadreTipoViolenciaOfTipoViolenciaCollectionTipoViolencia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoViolencia(tipoViolencia.getIdTipoViolencia()) != null) {
                throw new PreexistingEntityException("TipoViolencia " + tipoViolencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoViolencia tipoViolencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoViolencia persistentTipoViolencia = em.find(TipoViolencia.class, tipoViolencia.getIdTipoViolencia());
            TipoViolencia padreTipoViolenciaOld = persistentTipoViolencia.getPadreTipoViolencia();
            TipoViolencia padreTipoViolenciaNew = tipoViolencia.getPadreTipoViolencia();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionOld = persistentTipoViolencia.getAtencionEspecializadaCollection();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionNew = tipoViolencia.getAtencionEspecializadaCollection();
            Collection<TipoViolencia> tipoViolenciaCollectionOld = persistentTipoViolencia.getTipoViolenciaCollection();
            Collection<TipoViolencia> tipoViolenciaCollectionNew = tipoViolencia.getTipoViolenciaCollection();
            List<String> illegalOrphanMessages = null;
            for (TipoViolencia tipoViolenciaCollectionOldTipoViolencia : tipoViolenciaCollectionOld) {
                if (!tipoViolenciaCollectionNew.contains(tipoViolenciaCollectionOldTipoViolencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TipoViolencia " + tipoViolenciaCollectionOldTipoViolencia + " since its padreTipoViolencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (padreTipoViolenciaNew != null) {
                padreTipoViolenciaNew = em.getReference(padreTipoViolenciaNew.getClass(), padreTipoViolenciaNew.getIdTipoViolencia());
                tipoViolencia.setPadreTipoViolencia(padreTipoViolenciaNew);
            }
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollectionNew = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializadaToAttach : atencionEspecializadaCollectionNew) {
                atencionEspecializadaCollectionNewAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollectionNew.add(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach);
            }
            atencionEspecializadaCollectionNew = attachedAtencionEspecializadaCollectionNew;
            tipoViolencia.setAtencionEspecializadaCollection(atencionEspecializadaCollectionNew);
            Collection<TipoViolencia> attachedTipoViolenciaCollectionNew = new ArrayList<TipoViolencia>();
            for (TipoViolencia tipoViolenciaCollectionNewTipoViolenciaToAttach : tipoViolenciaCollectionNew) {
                tipoViolenciaCollectionNewTipoViolenciaToAttach = em.getReference(tipoViolenciaCollectionNewTipoViolenciaToAttach.getClass(), tipoViolenciaCollectionNewTipoViolenciaToAttach.getIdTipoViolencia());
                attachedTipoViolenciaCollectionNew.add(tipoViolenciaCollectionNewTipoViolenciaToAttach);
            }
            tipoViolenciaCollectionNew = attachedTipoViolenciaCollectionNew;
            tipoViolencia.setTipoViolenciaCollection(tipoViolenciaCollectionNew);
            tipoViolencia = em.merge(tipoViolencia);
            if (padreTipoViolenciaOld != null && !padreTipoViolenciaOld.equals(padreTipoViolenciaNew)) {
                padreTipoViolenciaOld.getTipoViolenciaCollection().remove(tipoViolencia);
                padreTipoViolenciaOld = em.merge(padreTipoViolenciaOld);
            }
            if (padreTipoViolenciaNew != null && !padreTipoViolenciaNew.equals(padreTipoViolenciaOld)) {
                padreTipoViolenciaNew.getTipoViolenciaCollection().add(tipoViolencia);
                padreTipoViolenciaNew = em.merge(padreTipoViolenciaNew);
            }
            for (AtencionEspecializada atencionEspecializadaCollectionOldAtencionEspecializada : atencionEspecializadaCollectionOld) {
                if (!atencionEspecializadaCollectionNew.contains(atencionEspecializadaCollectionOldAtencionEspecializada)) {
                    atencionEspecializadaCollectionOldAtencionEspecializada.getTipoViolenciaCollection().remove(tipoViolencia);
                    atencionEspecializadaCollectionOldAtencionEspecializada = em.merge(atencionEspecializadaCollectionOldAtencionEspecializada);
                }
            }
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializada : atencionEspecializadaCollectionNew) {
                if (!atencionEspecializadaCollectionOld.contains(atencionEspecializadaCollectionNewAtencionEspecializada)) {
                    atencionEspecializadaCollectionNewAtencionEspecializada.getTipoViolenciaCollection().add(tipoViolencia);
                    atencionEspecializadaCollectionNewAtencionEspecializada = em.merge(atencionEspecializadaCollectionNewAtencionEspecializada);
                }
            }
            for (TipoViolencia tipoViolenciaCollectionNewTipoViolencia : tipoViolenciaCollectionNew) {
                if (!tipoViolenciaCollectionOld.contains(tipoViolenciaCollectionNewTipoViolencia)) {
                    TipoViolencia oldPadreTipoViolenciaOfTipoViolenciaCollectionNewTipoViolencia = tipoViolenciaCollectionNewTipoViolencia.getPadreTipoViolencia();
                    tipoViolenciaCollectionNewTipoViolencia.setPadreTipoViolencia(tipoViolencia);
                    tipoViolenciaCollectionNewTipoViolencia = em.merge(tipoViolenciaCollectionNewTipoViolencia);
                    if (oldPadreTipoViolenciaOfTipoViolenciaCollectionNewTipoViolencia != null && !oldPadreTipoViolenciaOfTipoViolenciaCollectionNewTipoViolencia.equals(tipoViolencia)) {
                        oldPadreTipoViolenciaOfTipoViolenciaCollectionNewTipoViolencia.getTipoViolenciaCollection().remove(tipoViolenciaCollectionNewTipoViolencia);
                        oldPadreTipoViolenciaOfTipoViolenciaCollectionNewTipoViolencia = em.merge(oldPadreTipoViolenciaOfTipoViolenciaCollectionNewTipoViolencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoViolencia.getIdTipoViolencia();
                if (findTipoViolencia(id) == null) {
                    throw new NonexistentEntityException("The tipoViolencia with id " + id + " no longer exists.");
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
            TipoViolencia tipoViolencia;
            try {
                tipoViolencia = em.getReference(TipoViolencia.class, id);
                tipoViolencia.getIdTipoViolencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoViolencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TipoViolencia> tipoViolenciaCollectionOrphanCheck = tipoViolencia.getTipoViolenciaCollection();
            for (TipoViolencia tipoViolenciaCollectionOrphanCheckTipoViolencia : tipoViolenciaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoViolencia (" + tipoViolencia + ") cannot be destroyed since the TipoViolencia " + tipoViolenciaCollectionOrphanCheckTipoViolencia + " in its tipoViolenciaCollection field has a non-nullable padreTipoViolencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoViolencia padreTipoViolencia = tipoViolencia.getPadreTipoViolencia();
            if (padreTipoViolencia != null) {
                padreTipoViolencia.getTipoViolenciaCollection().remove(tipoViolencia);
                padreTipoViolencia = em.merge(padreTipoViolencia);
            }
            Collection<AtencionEspecializada> atencionEspecializadaCollection = tipoViolencia.getAtencionEspecializadaCollection();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : atencionEspecializadaCollection) {
                atencionEspecializadaCollectionAtencionEspecializada.getTipoViolenciaCollection().remove(tipoViolencia);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
            }
            em.remove(tipoViolencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoViolencia> findTipoViolenciaEntities() {
        return findTipoViolenciaEntities(true, -1, -1);
    }

    public List<TipoViolencia> findTipoViolenciaEntities(int maxResults, int firstResult) {
        return findTipoViolenciaEntities(false, maxResults, firstResult);
    }

    private List<TipoViolencia> findTipoViolenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoViolencia.class));
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

    public TipoViolencia findTipoViolencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoViolencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoViolenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoViolencia> rt = cq.from(TipoViolencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
