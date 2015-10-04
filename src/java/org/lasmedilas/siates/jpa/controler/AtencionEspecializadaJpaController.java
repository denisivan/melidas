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
import org.lasmedilas.siates.jpa.entidades.Municipio;
import org.lasmedilas.siates.jpa.entidades.TipoViolencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.IllegalOrphanException;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.Persona;
import org.lasmedilas.siates.jpa.entidades.Institucion;
import org.lasmedilas.siates.jpa.entidades.Asesoria;
import org.lasmedilas.siates.jpa.entidades.AtencionEspecializada;

/**
 *
 * @author Franco
 */
public class AtencionEspecializadaJpaController implements Serializable {

    public AtencionEspecializadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AtencionEspecializada atencionEspecializada) throws PreexistingEntityException, Exception {
        if (atencionEspecializada.getTipoViolenciaCollection() == null) {
            atencionEspecializada.setTipoViolenciaCollection(new ArrayList<TipoViolencia>());
        }
        if (atencionEspecializada.getPersonaCollection() == null) {
            atencionEspecializada.setPersonaCollection(new ArrayList<Persona>());
        }
        if (atencionEspecializada.getInstitucionCollection() == null) {
            atencionEspecializada.setInstitucionCollection(new ArrayList<Institucion>());
        }
        if (atencionEspecializada.getAsesoriaCollection() == null) {
            atencionEspecializada.setAsesoriaCollection(new ArrayList<Asesoria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio municipio = atencionEspecializada.getMunicipio();
            if (municipio != null) {
                municipio = em.getReference(municipio.getClass(), municipio.getMunicipioPK());
                atencionEspecializada.setMunicipio(municipio);
            }
            Collection<TipoViolencia> attachedTipoViolenciaCollection = new ArrayList<TipoViolencia>();
            for (TipoViolencia tipoViolenciaCollectionTipoViolenciaToAttach : atencionEspecializada.getTipoViolenciaCollection()) {
                tipoViolenciaCollectionTipoViolenciaToAttach = em.getReference(tipoViolenciaCollectionTipoViolenciaToAttach.getClass(), tipoViolenciaCollectionTipoViolenciaToAttach.getIdTipoViolencia());
                attachedTipoViolenciaCollection.add(tipoViolenciaCollectionTipoViolenciaToAttach);
            }
            atencionEspecializada.setTipoViolenciaCollection(attachedTipoViolenciaCollection);
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : atencionEspecializada.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getIdPersona());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            atencionEspecializada.setPersonaCollection(attachedPersonaCollection);
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : atencionEspecializada.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getIdInstitucion());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            atencionEspecializada.setInstitucionCollection(attachedInstitucionCollection);
            Collection<Asesoria> attachedAsesoriaCollection = new ArrayList<Asesoria>();
            for (Asesoria asesoriaCollectionAsesoriaToAttach : atencionEspecializada.getAsesoriaCollection()) {
                asesoriaCollectionAsesoriaToAttach = em.getReference(asesoriaCollectionAsesoriaToAttach.getClass(), asesoriaCollectionAsesoriaToAttach.getAsesoriaPK());
                attachedAsesoriaCollection.add(asesoriaCollectionAsesoriaToAttach);
            }
            atencionEspecializada.setAsesoriaCollection(attachedAsesoriaCollection);
            em.persist(atencionEspecializada);
            if (municipio != null) {
                municipio.getAtencionEspecializadaCollection().add(atencionEspecializada);
                municipio = em.merge(municipio);
            }
            for (TipoViolencia tipoViolenciaCollectionTipoViolencia : atencionEspecializada.getTipoViolenciaCollection()) {
                tipoViolenciaCollectionTipoViolencia.getAtencionEspecializadaCollection().add(atencionEspecializada);
                tipoViolenciaCollectionTipoViolencia = em.merge(tipoViolenciaCollectionTipoViolencia);
            }
            for (Persona personaCollectionPersona : atencionEspecializada.getPersonaCollection()) {
                personaCollectionPersona.getAtencionEspecializadaCollection().add(atencionEspecializada);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            for (Institucion institucionCollectionInstitucion : atencionEspecializada.getInstitucionCollection()) {
                institucionCollectionInstitucion.getAtencionEspecializadaCollection().add(atencionEspecializada);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            for (Asesoria asesoriaCollectionAsesoria : atencionEspecializada.getAsesoriaCollection()) {
                AtencionEspecializada oldAtencionEspecializadaOfAsesoriaCollectionAsesoria = asesoriaCollectionAsesoria.getAtencionEspecializada();
                asesoriaCollectionAsesoria.setAtencionEspecializada(atencionEspecializada);
                asesoriaCollectionAsesoria = em.merge(asesoriaCollectionAsesoria);
                if (oldAtencionEspecializadaOfAsesoriaCollectionAsesoria != null) {
                    oldAtencionEspecializadaOfAsesoriaCollectionAsesoria.getAsesoriaCollection().remove(asesoriaCollectionAsesoria);
                    oldAtencionEspecializadaOfAsesoriaCollectionAsesoria = em.merge(oldAtencionEspecializadaOfAsesoriaCollectionAsesoria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAtencionEspecializada(atencionEspecializada.getIdAtencionEspecializada()) != null) {
                throw new PreexistingEntityException("AtencionEspecializada " + atencionEspecializada + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AtencionEspecializada atencionEspecializada) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AtencionEspecializada persistentAtencionEspecializada = em.find(AtencionEspecializada.class, atencionEspecializada.getIdAtencionEspecializada());
            Municipio municipioOld = persistentAtencionEspecializada.getMunicipio();
            Municipio municipioNew = atencionEspecializada.getMunicipio();
            Collection<TipoViolencia> tipoViolenciaCollectionOld = persistentAtencionEspecializada.getTipoViolenciaCollection();
            Collection<TipoViolencia> tipoViolenciaCollectionNew = atencionEspecializada.getTipoViolenciaCollection();
            Collection<Persona> personaCollectionOld = persistentAtencionEspecializada.getPersonaCollection();
            Collection<Persona> personaCollectionNew = atencionEspecializada.getPersonaCollection();
            Collection<Institucion> institucionCollectionOld = persistentAtencionEspecializada.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = atencionEspecializada.getInstitucionCollection();
            Collection<Asesoria> asesoriaCollectionOld = persistentAtencionEspecializada.getAsesoriaCollection();
            Collection<Asesoria> asesoriaCollectionNew = atencionEspecializada.getAsesoriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Asesoria asesoriaCollectionOldAsesoria : asesoriaCollectionOld) {
                if (!asesoriaCollectionNew.contains(asesoriaCollectionOldAsesoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asesoria " + asesoriaCollectionOldAsesoria + " since its atencionEspecializada field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (municipioNew != null) {
                municipioNew = em.getReference(municipioNew.getClass(), municipioNew.getMunicipioPK());
                atencionEspecializada.setMunicipio(municipioNew);
            }
            Collection<TipoViolencia> attachedTipoViolenciaCollectionNew = new ArrayList<TipoViolencia>();
            for (TipoViolencia tipoViolenciaCollectionNewTipoViolenciaToAttach : tipoViolenciaCollectionNew) {
                tipoViolenciaCollectionNewTipoViolenciaToAttach = em.getReference(tipoViolenciaCollectionNewTipoViolenciaToAttach.getClass(), tipoViolenciaCollectionNewTipoViolenciaToAttach.getIdTipoViolencia());
                attachedTipoViolenciaCollectionNew.add(tipoViolenciaCollectionNewTipoViolenciaToAttach);
            }
            tipoViolenciaCollectionNew = attachedTipoViolenciaCollectionNew;
            atencionEspecializada.setTipoViolenciaCollection(tipoViolenciaCollectionNew);
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getIdPersona());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            atencionEspecializada.setPersonaCollection(personaCollectionNew);
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getIdInstitucion());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            atencionEspecializada.setInstitucionCollection(institucionCollectionNew);
            Collection<Asesoria> attachedAsesoriaCollectionNew = new ArrayList<Asesoria>();
            for (Asesoria asesoriaCollectionNewAsesoriaToAttach : asesoriaCollectionNew) {
                asesoriaCollectionNewAsesoriaToAttach = em.getReference(asesoriaCollectionNewAsesoriaToAttach.getClass(), asesoriaCollectionNewAsesoriaToAttach.getAsesoriaPK());
                attachedAsesoriaCollectionNew.add(asesoriaCollectionNewAsesoriaToAttach);
            }
            asesoriaCollectionNew = attachedAsesoriaCollectionNew;
            atencionEspecializada.setAsesoriaCollection(asesoriaCollectionNew);
            atencionEspecializada = em.merge(atencionEspecializada);
            if (municipioOld != null && !municipioOld.equals(municipioNew)) {
                municipioOld.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                municipioOld = em.merge(municipioOld);
            }
            if (municipioNew != null && !municipioNew.equals(municipioOld)) {
                municipioNew.getAtencionEspecializadaCollection().add(atencionEspecializada);
                municipioNew = em.merge(municipioNew);
            }
            for (TipoViolencia tipoViolenciaCollectionOldTipoViolencia : tipoViolenciaCollectionOld) {
                if (!tipoViolenciaCollectionNew.contains(tipoViolenciaCollectionOldTipoViolencia)) {
                    tipoViolenciaCollectionOldTipoViolencia.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                    tipoViolenciaCollectionOldTipoViolencia = em.merge(tipoViolenciaCollectionOldTipoViolencia);
                }
            }
            for (TipoViolencia tipoViolenciaCollectionNewTipoViolencia : tipoViolenciaCollectionNew) {
                if (!tipoViolenciaCollectionOld.contains(tipoViolenciaCollectionNewTipoViolencia)) {
                    tipoViolenciaCollectionNewTipoViolencia.getAtencionEspecializadaCollection().add(atencionEspecializada);
                    tipoViolenciaCollectionNewTipoViolencia = em.merge(tipoViolenciaCollectionNewTipoViolencia);
                }
            }
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    personaCollectionOldPersona.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                    personaCollectionOldPersona = em.merge(personaCollectionOldPersona);
                }
            }
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    personaCollectionNewPersona.getAtencionEspecializadaCollection().add(atencionEspecializada);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                }
            }
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    institucionCollectionNewInstitucion.getAtencionEspecializadaCollection().add(atencionEspecializada);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                }
            }
            for (Asesoria asesoriaCollectionNewAsesoria : asesoriaCollectionNew) {
                if (!asesoriaCollectionOld.contains(asesoriaCollectionNewAsesoria)) {
                    AtencionEspecializada oldAtencionEspecializadaOfAsesoriaCollectionNewAsesoria = asesoriaCollectionNewAsesoria.getAtencionEspecializada();
                    asesoriaCollectionNewAsesoria.setAtencionEspecializada(atencionEspecializada);
                    asesoriaCollectionNewAsesoria = em.merge(asesoriaCollectionNewAsesoria);
                    if (oldAtencionEspecializadaOfAsesoriaCollectionNewAsesoria != null && !oldAtencionEspecializadaOfAsesoriaCollectionNewAsesoria.equals(atencionEspecializada)) {
                        oldAtencionEspecializadaOfAsesoriaCollectionNewAsesoria.getAsesoriaCollection().remove(asesoriaCollectionNewAsesoria);
                        oldAtencionEspecializadaOfAsesoriaCollectionNewAsesoria = em.merge(oldAtencionEspecializadaOfAsesoriaCollectionNewAsesoria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = atencionEspecializada.getIdAtencionEspecializada();
                if (findAtencionEspecializada(id) == null) {
                    throw new NonexistentEntityException("The atencionEspecializada with id " + id + " no longer exists.");
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
            AtencionEspecializada atencionEspecializada;
            try {
                atencionEspecializada = em.getReference(AtencionEspecializada.class, id);
                atencionEspecializada.getIdAtencionEspecializada();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atencionEspecializada with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Asesoria> asesoriaCollectionOrphanCheck = atencionEspecializada.getAsesoriaCollection();
            for (Asesoria asesoriaCollectionOrphanCheckAsesoria : asesoriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AtencionEspecializada (" + atencionEspecializada + ") cannot be destroyed since the Asesoria " + asesoriaCollectionOrphanCheckAsesoria + " in its asesoriaCollection field has a non-nullable atencionEspecializada field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Municipio municipio = atencionEspecializada.getMunicipio();
            if (municipio != null) {
                municipio.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                municipio = em.merge(municipio);
            }
            Collection<TipoViolencia> tipoViolenciaCollection = atencionEspecializada.getTipoViolenciaCollection();
            for (TipoViolencia tipoViolenciaCollectionTipoViolencia : tipoViolenciaCollection) {
                tipoViolenciaCollectionTipoViolencia.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                tipoViolenciaCollectionTipoViolencia = em.merge(tipoViolenciaCollectionTipoViolencia);
            }
            Collection<Persona> personaCollection = atencionEspecializada.getPersonaCollection();
            for (Persona personaCollectionPersona : personaCollection) {
                personaCollectionPersona.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            Collection<Institucion> institucionCollection = atencionEspecializada.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.getAtencionEspecializadaCollection().remove(atencionEspecializada);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(atencionEspecializada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AtencionEspecializada> findAtencionEspecializadaEntities() {
        return findAtencionEspecializadaEntities(true, -1, -1);
    }

    public List<AtencionEspecializada> findAtencionEspecializadaEntities(int maxResults, int firstResult) {
        return findAtencionEspecializadaEntities(false, maxResults, firstResult);
    }

    private List<AtencionEspecializada> findAtencionEspecializadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AtencionEspecializada.class));
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

    public AtencionEspecializada findAtencionEspecializada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AtencionEspecializada.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtencionEspecializadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AtencionEspecializada> rt = cq.from(AtencionEspecializada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
