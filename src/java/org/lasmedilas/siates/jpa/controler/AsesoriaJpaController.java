/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.controler;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.Asesoria;
import org.lasmedilas.siates.jpa.entidades.AsesoriaPK;
import org.lasmedilas.siates.jpa.entidades.AtencionEspecializada;
import org.lasmedilas.siates.jpa.entidades.Modalidad;
import org.lasmedilas.siates.jpa.entidades.TipoAsesoria;

/**
 *
 * @author Franco
 */
public class AsesoriaJpaController implements Serializable {

    public AsesoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asesoria asesoria) throws PreexistingEntityException, Exception {
        if (asesoria.getAsesoriaPK() == null) {
            asesoria.setAsesoriaPK(new AsesoriaPK());
        }
        asesoria.getAsesoriaPK().setIdTipoAsesoria(asesoria.getTipoAsesoria().getIdTipoAsesoria());
        asesoria.getAsesoriaPK().setIdModalidad(asesoria.getModalidad().getIdModalidad());
        asesoria.getAsesoriaPK().setIdAtencionEspecializada(asesoria.getAtencionEspecializada().getIdAtencionEspecializada());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AtencionEspecializada atencionEspecializada = asesoria.getAtencionEspecializada();
            if (atencionEspecializada != null) {
                atencionEspecializada = em.getReference(atencionEspecializada.getClass(), atencionEspecializada.getIdAtencionEspecializada());
                asesoria.setAtencionEspecializada(atencionEspecializada);
            }
            Modalidad modalidad = asesoria.getModalidad();
            if (modalidad != null) {
                modalidad = em.getReference(modalidad.getClass(), modalidad.getIdModalidad());
                asesoria.setModalidad(modalidad);
            }
            TipoAsesoria tipoAsesoria = asesoria.getTipoAsesoria();
            if (tipoAsesoria != null) {
                tipoAsesoria = em.getReference(tipoAsesoria.getClass(), tipoAsesoria.getIdTipoAsesoria());
                asesoria.setTipoAsesoria(tipoAsesoria);
            }
            em.persist(asesoria);
            if (atencionEspecializada != null) {
                atencionEspecializada.getAsesoriaCollection().add(asesoria);
                atencionEspecializada = em.merge(atencionEspecializada);
            }
            if (modalidad != null) {
                modalidad.getAsesoriaCollection().add(asesoria);
                modalidad = em.merge(modalidad);
            }
            if (tipoAsesoria != null) {
                tipoAsesoria.getAsesoriaCollection().add(asesoria);
                tipoAsesoria = em.merge(tipoAsesoria);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAsesoria(asesoria.getAsesoriaPK()) != null) {
                throw new PreexistingEntityException("Asesoria " + asesoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asesoria asesoria) throws NonexistentEntityException, Exception {
        asesoria.getAsesoriaPK().setIdTipoAsesoria(asesoria.getTipoAsesoria().getIdTipoAsesoria());
        asesoria.getAsesoriaPK().setIdModalidad(asesoria.getModalidad().getIdModalidad());
        asesoria.getAsesoriaPK().setIdAtencionEspecializada(asesoria.getAtencionEspecializada().getIdAtencionEspecializada());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asesoria persistentAsesoria = em.find(Asesoria.class, asesoria.getAsesoriaPK());
            AtencionEspecializada atencionEspecializadaOld = persistentAsesoria.getAtencionEspecializada();
            AtencionEspecializada atencionEspecializadaNew = asesoria.getAtencionEspecializada();
            Modalidad modalidadOld = persistentAsesoria.getModalidad();
            Modalidad modalidadNew = asesoria.getModalidad();
            TipoAsesoria tipoAsesoriaOld = persistentAsesoria.getTipoAsesoria();
            TipoAsesoria tipoAsesoriaNew = asesoria.getTipoAsesoria();
            if (atencionEspecializadaNew != null) {
                atencionEspecializadaNew = em.getReference(atencionEspecializadaNew.getClass(), atencionEspecializadaNew.getIdAtencionEspecializada());
                asesoria.setAtencionEspecializada(atencionEspecializadaNew);
            }
            if (modalidadNew != null) {
                modalidadNew = em.getReference(modalidadNew.getClass(), modalidadNew.getIdModalidad());
                asesoria.setModalidad(modalidadNew);
            }
            if (tipoAsesoriaNew != null) {
                tipoAsesoriaNew = em.getReference(tipoAsesoriaNew.getClass(), tipoAsesoriaNew.getIdTipoAsesoria());
                asesoria.setTipoAsesoria(tipoAsesoriaNew);
            }
            asesoria = em.merge(asesoria);
            if (atencionEspecializadaOld != null && !atencionEspecializadaOld.equals(atencionEspecializadaNew)) {
                atencionEspecializadaOld.getAsesoriaCollection().remove(asesoria);
                atencionEspecializadaOld = em.merge(atencionEspecializadaOld);
            }
            if (atencionEspecializadaNew != null && !atencionEspecializadaNew.equals(atencionEspecializadaOld)) {
                atencionEspecializadaNew.getAsesoriaCollection().add(asesoria);
                atencionEspecializadaNew = em.merge(atencionEspecializadaNew);
            }
            if (modalidadOld != null && !modalidadOld.equals(modalidadNew)) {
                modalidadOld.getAsesoriaCollection().remove(asesoria);
                modalidadOld = em.merge(modalidadOld);
            }
            if (modalidadNew != null && !modalidadNew.equals(modalidadOld)) {
                modalidadNew.getAsesoriaCollection().add(asesoria);
                modalidadNew = em.merge(modalidadNew);
            }
            if (tipoAsesoriaOld != null && !tipoAsesoriaOld.equals(tipoAsesoriaNew)) {
                tipoAsesoriaOld.getAsesoriaCollection().remove(asesoria);
                tipoAsesoriaOld = em.merge(tipoAsesoriaOld);
            }
            if (tipoAsesoriaNew != null && !tipoAsesoriaNew.equals(tipoAsesoriaOld)) {
                tipoAsesoriaNew.getAsesoriaCollection().add(asesoria);
                tipoAsesoriaNew = em.merge(tipoAsesoriaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AsesoriaPK id = asesoria.getAsesoriaPK();
                if (findAsesoria(id) == null) {
                    throw new NonexistentEntityException("The asesoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AsesoriaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asesoria asesoria;
            try {
                asesoria = em.getReference(Asesoria.class, id);
                asesoria.getAsesoriaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asesoria with id " + id + " no longer exists.", enfe);
            }
            AtencionEspecializada atencionEspecializada = asesoria.getAtencionEspecializada();
            if (atencionEspecializada != null) {
                atencionEspecializada.getAsesoriaCollection().remove(asesoria);
                atencionEspecializada = em.merge(atencionEspecializada);
            }
            Modalidad modalidad = asesoria.getModalidad();
            if (modalidad != null) {
                modalidad.getAsesoriaCollection().remove(asesoria);
                modalidad = em.merge(modalidad);
            }
            TipoAsesoria tipoAsesoria = asesoria.getTipoAsesoria();
            if (tipoAsesoria != null) {
                tipoAsesoria.getAsesoriaCollection().remove(asesoria);
                tipoAsesoria = em.merge(tipoAsesoria);
            }
            em.remove(asesoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asesoria> findAsesoriaEntities() {
        return findAsesoriaEntities(true, -1, -1);
    }

    public List<Asesoria> findAsesoriaEntities(int maxResults, int firstResult) {
        return findAsesoriaEntities(false, maxResults, firstResult);
    }

    private List<Asesoria> findAsesoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asesoria.class));
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

    public Asesoria findAsesoria(AsesoriaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asesoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsesoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asesoria> rt = cq.from(Asesoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
