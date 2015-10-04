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
import org.lasmedilas.siates.jpa.entidades.AtencionEspecializada;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.Persona;
import org.lasmedilas.siates.jpa.entidades.Referencia;
import org.lasmedilas.siates.jpa.entidades.TipoAgresor;
import org.lasmedilas.siates.jpa.entidades.Profesion;

/**
 *
 * @author Franco
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        if (persona.getAtencionEspecializadaCollection() == null) {
            persona.setAtencionEspecializadaCollection(new ArrayList<AtencionEspecializada>());
        }
        if (persona.getReferenciaCollection() == null) {
            persona.setReferenciaCollection(new ArrayList<Referencia>());
        }
        if (persona.getTipoAgresorCollection() == null) {
            persona.setTipoAgresorCollection(new ArrayList<TipoAgresor>());
        }
        if (persona.getProfesionCollection() == null) {
            persona.setProfesionCollection(new ArrayList<Profesion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio municipio = persona.getMunicipio();
            if (municipio != null) {
                municipio = em.getReference(municipio.getClass(), municipio.getMunicipioPK());
                persona.setMunicipio(municipio);
            }
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollection = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializadaToAttach : persona.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollection.add(atencionEspecializadaCollectionAtencionEspecializadaToAttach);
            }
            persona.setAtencionEspecializadaCollection(attachedAtencionEspecializadaCollection);
            Collection<Referencia> attachedReferenciaCollection = new ArrayList<Referencia>();
            for (Referencia referenciaCollectionReferenciaToAttach : persona.getReferenciaCollection()) {
                referenciaCollectionReferenciaToAttach = em.getReference(referenciaCollectionReferenciaToAttach.getClass(), referenciaCollectionReferenciaToAttach.getIdReferencia());
                attachedReferenciaCollection.add(referenciaCollectionReferenciaToAttach);
            }
            persona.setReferenciaCollection(attachedReferenciaCollection);
            Collection<TipoAgresor> attachedTipoAgresorCollection = new ArrayList<TipoAgresor>();
            for (TipoAgresor tipoAgresorCollectionTipoAgresorToAttach : persona.getTipoAgresorCollection()) {
                tipoAgresorCollectionTipoAgresorToAttach = em.getReference(tipoAgresorCollectionTipoAgresorToAttach.getClass(), tipoAgresorCollectionTipoAgresorToAttach.getIdTipoAgresor());
                attachedTipoAgresorCollection.add(tipoAgresorCollectionTipoAgresorToAttach);
            }
            persona.setTipoAgresorCollection(attachedTipoAgresorCollection);
            Collection<Profesion> attachedProfesionCollection = new ArrayList<Profesion>();
            for (Profesion profesionCollectionProfesionToAttach : persona.getProfesionCollection()) {
                profesionCollectionProfesionToAttach = em.getReference(profesionCollectionProfesionToAttach.getClass(), profesionCollectionProfesionToAttach.getIdProfesion());
                attachedProfesionCollection.add(profesionCollectionProfesionToAttach);
            }
            persona.setProfesionCollection(attachedProfesionCollection);
            em.persist(persona);
            if (municipio != null) {
                municipio.getPersonaCollection().add(persona);
                municipio = em.merge(municipio);
            }
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : persona.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializada.getPersonaCollection().add(persona);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
            }
            for (Referencia referenciaCollectionReferencia : persona.getReferenciaCollection()) {
                referenciaCollectionReferencia.getPersonaCollection().add(persona);
                referenciaCollectionReferencia = em.merge(referenciaCollectionReferencia);
            }
            for (TipoAgresor tipoAgresorCollectionTipoAgresor : persona.getTipoAgresorCollection()) {
                tipoAgresorCollectionTipoAgresor.getPersonaCollection().add(persona);
                tipoAgresorCollectionTipoAgresor = em.merge(tipoAgresorCollectionTipoAgresor);
            }
            for (Profesion profesionCollectionProfesion : persona.getProfesionCollection()) {
                profesionCollectionProfesion.getPersonaCollection().add(persona);
                profesionCollectionProfesion = em.merge(profesionCollectionProfesion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getIdPersona()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            Municipio municipioOld = persistentPersona.getMunicipio();
            Municipio municipioNew = persona.getMunicipio();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionOld = persistentPersona.getAtencionEspecializadaCollection();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionNew = persona.getAtencionEspecializadaCollection();
            Collection<Referencia> referenciaCollectionOld = persistentPersona.getReferenciaCollection();
            Collection<Referencia> referenciaCollectionNew = persona.getReferenciaCollection();
            Collection<TipoAgresor> tipoAgresorCollectionOld = persistentPersona.getTipoAgresorCollection();
            Collection<TipoAgresor> tipoAgresorCollectionNew = persona.getTipoAgresorCollection();
            Collection<Profesion> profesionCollectionOld = persistentPersona.getProfesionCollection();
            Collection<Profesion> profesionCollectionNew = persona.getProfesionCollection();
            if (municipioNew != null) {
                municipioNew = em.getReference(municipioNew.getClass(), municipioNew.getMunicipioPK());
                persona.setMunicipio(municipioNew);
            }
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollectionNew = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializadaToAttach : atencionEspecializadaCollectionNew) {
                atencionEspecializadaCollectionNewAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollectionNew.add(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach);
            }
            atencionEspecializadaCollectionNew = attachedAtencionEspecializadaCollectionNew;
            persona.setAtencionEspecializadaCollection(atencionEspecializadaCollectionNew);
            Collection<Referencia> attachedReferenciaCollectionNew = new ArrayList<Referencia>();
            for (Referencia referenciaCollectionNewReferenciaToAttach : referenciaCollectionNew) {
                referenciaCollectionNewReferenciaToAttach = em.getReference(referenciaCollectionNewReferenciaToAttach.getClass(), referenciaCollectionNewReferenciaToAttach.getIdReferencia());
                attachedReferenciaCollectionNew.add(referenciaCollectionNewReferenciaToAttach);
            }
            referenciaCollectionNew = attachedReferenciaCollectionNew;
            persona.setReferenciaCollection(referenciaCollectionNew);
            Collection<TipoAgresor> attachedTipoAgresorCollectionNew = new ArrayList<TipoAgresor>();
            for (TipoAgresor tipoAgresorCollectionNewTipoAgresorToAttach : tipoAgresorCollectionNew) {
                tipoAgresorCollectionNewTipoAgresorToAttach = em.getReference(tipoAgresorCollectionNewTipoAgresorToAttach.getClass(), tipoAgresorCollectionNewTipoAgresorToAttach.getIdTipoAgresor());
                attachedTipoAgresorCollectionNew.add(tipoAgresorCollectionNewTipoAgresorToAttach);
            }
            tipoAgresorCollectionNew = attachedTipoAgresorCollectionNew;
            persona.setTipoAgresorCollection(tipoAgresorCollectionNew);
            Collection<Profesion> attachedProfesionCollectionNew = new ArrayList<Profesion>();
            for (Profesion profesionCollectionNewProfesionToAttach : profesionCollectionNew) {
                profesionCollectionNewProfesionToAttach = em.getReference(profesionCollectionNewProfesionToAttach.getClass(), profesionCollectionNewProfesionToAttach.getIdProfesion());
                attachedProfesionCollectionNew.add(profesionCollectionNewProfesionToAttach);
            }
            profesionCollectionNew = attachedProfesionCollectionNew;
            persona.setProfesionCollection(profesionCollectionNew);
            persona = em.merge(persona);
            if (municipioOld != null && !municipioOld.equals(municipioNew)) {
                municipioOld.getPersonaCollection().remove(persona);
                municipioOld = em.merge(municipioOld);
            }
            if (municipioNew != null && !municipioNew.equals(municipioOld)) {
                municipioNew.getPersonaCollection().add(persona);
                municipioNew = em.merge(municipioNew);
            }
            for (AtencionEspecializada atencionEspecializadaCollectionOldAtencionEspecializada : atencionEspecializadaCollectionOld) {
                if (!atencionEspecializadaCollectionNew.contains(atencionEspecializadaCollectionOldAtencionEspecializada)) {
                    atencionEspecializadaCollectionOldAtencionEspecializada.getPersonaCollection().remove(persona);
                    atencionEspecializadaCollectionOldAtencionEspecializada = em.merge(atencionEspecializadaCollectionOldAtencionEspecializada);
                }
            }
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializada : atencionEspecializadaCollectionNew) {
                if (!atencionEspecializadaCollectionOld.contains(atencionEspecializadaCollectionNewAtencionEspecializada)) {
                    atencionEspecializadaCollectionNewAtencionEspecializada.getPersonaCollection().add(persona);
                    atencionEspecializadaCollectionNewAtencionEspecializada = em.merge(atencionEspecializadaCollectionNewAtencionEspecializada);
                }
            }
            for (Referencia referenciaCollectionOldReferencia : referenciaCollectionOld) {
                if (!referenciaCollectionNew.contains(referenciaCollectionOldReferencia)) {
                    referenciaCollectionOldReferencia.getPersonaCollection().remove(persona);
                    referenciaCollectionOldReferencia = em.merge(referenciaCollectionOldReferencia);
                }
            }
            for (Referencia referenciaCollectionNewReferencia : referenciaCollectionNew) {
                if (!referenciaCollectionOld.contains(referenciaCollectionNewReferencia)) {
                    referenciaCollectionNewReferencia.getPersonaCollection().add(persona);
                    referenciaCollectionNewReferencia = em.merge(referenciaCollectionNewReferencia);
                }
            }
            for (TipoAgresor tipoAgresorCollectionOldTipoAgresor : tipoAgresorCollectionOld) {
                if (!tipoAgresorCollectionNew.contains(tipoAgresorCollectionOldTipoAgresor)) {
                    tipoAgresorCollectionOldTipoAgresor.getPersonaCollection().remove(persona);
                    tipoAgresorCollectionOldTipoAgresor = em.merge(tipoAgresorCollectionOldTipoAgresor);
                }
            }
            for (TipoAgresor tipoAgresorCollectionNewTipoAgresor : tipoAgresorCollectionNew) {
                if (!tipoAgresorCollectionOld.contains(tipoAgresorCollectionNewTipoAgresor)) {
                    tipoAgresorCollectionNewTipoAgresor.getPersonaCollection().add(persona);
                    tipoAgresorCollectionNewTipoAgresor = em.merge(tipoAgresorCollectionNewTipoAgresor);
                }
            }
            for (Profesion profesionCollectionOldProfesion : profesionCollectionOld) {
                if (!profesionCollectionNew.contains(profesionCollectionOldProfesion)) {
                    profesionCollectionOldProfesion.getPersonaCollection().remove(persona);
                    profesionCollectionOldProfesion = em.merge(profesionCollectionOldProfesion);
                }
            }
            for (Profesion profesionCollectionNewProfesion : profesionCollectionNew) {
                if (!profesionCollectionOld.contains(profesionCollectionNewProfesion)) {
                    profesionCollectionNewProfesion.getPersonaCollection().add(persona);
                    profesionCollectionNewProfesion = em.merge(profesionCollectionNewProfesion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getIdPersona();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            Municipio municipio = persona.getMunicipio();
            if (municipio != null) {
                municipio.getPersonaCollection().remove(persona);
                municipio = em.merge(municipio);
            }
            Collection<AtencionEspecializada> atencionEspecializadaCollection = persona.getAtencionEspecializadaCollection();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : atencionEspecializadaCollection) {
                atencionEspecializadaCollectionAtencionEspecializada.getPersonaCollection().remove(persona);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
            }
            Collection<Referencia> referenciaCollection = persona.getReferenciaCollection();
            for (Referencia referenciaCollectionReferencia : referenciaCollection) {
                referenciaCollectionReferencia.getPersonaCollection().remove(persona);
                referenciaCollectionReferencia = em.merge(referenciaCollectionReferencia);
            }
            Collection<TipoAgresor> tipoAgresorCollection = persona.getTipoAgresorCollection();
            for (TipoAgresor tipoAgresorCollectionTipoAgresor : tipoAgresorCollection) {
                tipoAgresorCollectionTipoAgresor.getPersonaCollection().remove(persona);
                tipoAgresorCollectionTipoAgresor = em.merge(tipoAgresorCollectionTipoAgresor);
            }
            Collection<Profesion> profesionCollection = persona.getProfesionCollection();
            for (Profesion profesionCollectionProfesion : profesionCollection) {
                profesionCollectionProfesion.getPersonaCollection().remove(persona);
                profesionCollectionProfesion = em.merge(profesionCollectionProfesion);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
