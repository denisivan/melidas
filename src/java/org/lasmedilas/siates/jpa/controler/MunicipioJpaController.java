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
import org.lasmedilas.siates.jpa.entidades.Departamento;
import org.lasmedilas.siates.jpa.entidades.Persona;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.IllegalOrphanException;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.AtencionEspecializada;
import org.lasmedilas.siates.jpa.entidades.Municipio;
import org.lasmedilas.siates.jpa.entidades.MunicipioPK;

/**
 *
 * @author Franco
 */
public class MunicipioJpaController implements Serializable {

    public MunicipioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Municipio municipio) throws PreexistingEntityException, Exception {
        if (municipio.getMunicipioPK() == null) {
            municipio.setMunicipioPK(new MunicipioPK());
        }
        if (municipio.getPersonaCollection() == null) {
            municipio.setPersonaCollection(new ArrayList<Persona>());
        }
        if (municipio.getAtencionEspecializadaCollection() == null) {
            municipio.setAtencionEspecializadaCollection(new ArrayList<AtencionEspecializada>());
        }
        municipio.getMunicipioPK().setIdDepartamento(municipio.getDepartamento().getIdDepartamento());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamento = municipio.getDepartamento();
            if (departamento != null) {
                departamento = em.getReference(departamento.getClass(), departamento.getIdDepartamento());
                municipio.setDepartamento(departamento);
            }
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : municipio.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getIdPersona());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            municipio.setPersonaCollection(attachedPersonaCollection);
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollection = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializadaToAttach : municipio.getAtencionEspecializadaCollection()) {
                atencionEspecializadaCollectionAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollection.add(atencionEspecializadaCollectionAtencionEspecializadaToAttach);
            }
            municipio.setAtencionEspecializadaCollection(attachedAtencionEspecializadaCollection);
            em.persist(municipio);
            if (departamento != null) {
                departamento.getMunicipioCollection().add(municipio);
                departamento = em.merge(departamento);
            }
            for (Persona personaCollectionPersona : municipio.getPersonaCollection()) {
                Municipio oldMunicipioOfPersonaCollectionPersona = personaCollectionPersona.getMunicipio();
                personaCollectionPersona.setMunicipio(municipio);
                personaCollectionPersona = em.merge(personaCollectionPersona);
                if (oldMunicipioOfPersonaCollectionPersona != null) {
                    oldMunicipioOfPersonaCollectionPersona.getPersonaCollection().remove(personaCollectionPersona);
                    oldMunicipioOfPersonaCollectionPersona = em.merge(oldMunicipioOfPersonaCollectionPersona);
                }
            }
            for (AtencionEspecializada atencionEspecializadaCollectionAtencionEspecializada : municipio.getAtencionEspecializadaCollection()) {
                Municipio oldMunicipioOfAtencionEspecializadaCollectionAtencionEspecializada = atencionEspecializadaCollectionAtencionEspecializada.getMunicipio();
                atencionEspecializadaCollectionAtencionEspecializada.setMunicipio(municipio);
                atencionEspecializadaCollectionAtencionEspecializada = em.merge(atencionEspecializadaCollectionAtencionEspecializada);
                if (oldMunicipioOfAtencionEspecializadaCollectionAtencionEspecializada != null) {
                    oldMunicipioOfAtencionEspecializadaCollectionAtencionEspecializada.getAtencionEspecializadaCollection().remove(atencionEspecializadaCollectionAtencionEspecializada);
                    oldMunicipioOfAtencionEspecializadaCollectionAtencionEspecializada = em.merge(oldMunicipioOfAtencionEspecializadaCollectionAtencionEspecializada);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMunicipio(municipio.getMunicipioPK()) != null) {
                throw new PreexistingEntityException("Municipio " + municipio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Municipio municipio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        municipio.getMunicipioPK().setIdDepartamento(municipio.getDepartamento().getIdDepartamento());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio persistentMunicipio = em.find(Municipio.class, municipio.getMunicipioPK());
            Departamento departamentoOld = persistentMunicipio.getDepartamento();
            Departamento departamentoNew = municipio.getDepartamento();
            Collection<Persona> personaCollectionOld = persistentMunicipio.getPersonaCollection();
            Collection<Persona> personaCollectionNew = municipio.getPersonaCollection();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionOld = persistentMunicipio.getAtencionEspecializadaCollection();
            Collection<AtencionEspecializada> atencionEspecializadaCollectionNew = municipio.getAtencionEspecializadaCollection();
            List<String> illegalOrphanMessages = null;
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Persona " + personaCollectionOldPersona + " since its municipio field is not nullable.");
                }
            }
            for (AtencionEspecializada atencionEspecializadaCollectionOldAtencionEspecializada : atencionEspecializadaCollectionOld) {
                if (!atencionEspecializadaCollectionNew.contains(atencionEspecializadaCollectionOldAtencionEspecializada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AtencionEspecializada " + atencionEspecializadaCollectionOldAtencionEspecializada + " since its municipio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departamentoNew != null) {
                departamentoNew = em.getReference(departamentoNew.getClass(), departamentoNew.getIdDepartamento());
                municipio.setDepartamento(departamentoNew);
            }
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getIdPersona());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            municipio.setPersonaCollection(personaCollectionNew);
            Collection<AtencionEspecializada> attachedAtencionEspecializadaCollectionNew = new ArrayList<AtencionEspecializada>();
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializadaToAttach : atencionEspecializadaCollectionNew) {
                atencionEspecializadaCollectionNewAtencionEspecializadaToAttach = em.getReference(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getClass(), atencionEspecializadaCollectionNewAtencionEspecializadaToAttach.getIdAtencionEspecializada());
                attachedAtencionEspecializadaCollectionNew.add(atencionEspecializadaCollectionNewAtencionEspecializadaToAttach);
            }
            atencionEspecializadaCollectionNew = attachedAtencionEspecializadaCollectionNew;
            municipio.setAtencionEspecializadaCollection(atencionEspecializadaCollectionNew);
            municipio = em.merge(municipio);
            if (departamentoOld != null && !departamentoOld.equals(departamentoNew)) {
                departamentoOld.getMunicipioCollection().remove(municipio);
                departamentoOld = em.merge(departamentoOld);
            }
            if (departamentoNew != null && !departamentoNew.equals(departamentoOld)) {
                departamentoNew.getMunicipioCollection().add(municipio);
                departamentoNew = em.merge(departamentoNew);
            }
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    Municipio oldMunicipioOfPersonaCollectionNewPersona = personaCollectionNewPersona.getMunicipio();
                    personaCollectionNewPersona.setMunicipio(municipio);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                    if (oldMunicipioOfPersonaCollectionNewPersona != null && !oldMunicipioOfPersonaCollectionNewPersona.equals(municipio)) {
                        oldMunicipioOfPersonaCollectionNewPersona.getPersonaCollection().remove(personaCollectionNewPersona);
                        oldMunicipioOfPersonaCollectionNewPersona = em.merge(oldMunicipioOfPersonaCollectionNewPersona);
                    }
                }
            }
            for (AtencionEspecializada atencionEspecializadaCollectionNewAtencionEspecializada : atencionEspecializadaCollectionNew) {
                if (!atencionEspecializadaCollectionOld.contains(atencionEspecializadaCollectionNewAtencionEspecializada)) {
                    Municipio oldMunicipioOfAtencionEspecializadaCollectionNewAtencionEspecializada = atencionEspecializadaCollectionNewAtencionEspecializada.getMunicipio();
                    atencionEspecializadaCollectionNewAtencionEspecializada.setMunicipio(municipio);
                    atencionEspecializadaCollectionNewAtencionEspecializada = em.merge(atencionEspecializadaCollectionNewAtencionEspecializada);
                    if (oldMunicipioOfAtencionEspecializadaCollectionNewAtencionEspecializada != null && !oldMunicipioOfAtencionEspecializadaCollectionNewAtencionEspecializada.equals(municipio)) {
                        oldMunicipioOfAtencionEspecializadaCollectionNewAtencionEspecializada.getAtencionEspecializadaCollection().remove(atencionEspecializadaCollectionNewAtencionEspecializada);
                        oldMunicipioOfAtencionEspecializadaCollectionNewAtencionEspecializada = em.merge(oldMunicipioOfAtencionEspecializadaCollectionNewAtencionEspecializada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MunicipioPK id = municipio.getMunicipioPK();
                if (findMunicipio(id) == null) {
                    throw new NonexistentEntityException("The municipio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MunicipioPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio municipio;
            try {
                municipio = em.getReference(Municipio.class, id);
                municipio.getMunicipioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Persona> personaCollectionOrphanCheck = municipio.getPersonaCollection();
            for (Persona personaCollectionOrphanCheckPersona : personaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Municipio (" + municipio + ") cannot be destroyed since the Persona " + personaCollectionOrphanCheckPersona + " in its personaCollection field has a non-nullable municipio field.");
            }
            Collection<AtencionEspecializada> atencionEspecializadaCollectionOrphanCheck = municipio.getAtencionEspecializadaCollection();
            for (AtencionEspecializada atencionEspecializadaCollectionOrphanCheckAtencionEspecializada : atencionEspecializadaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Municipio (" + municipio + ") cannot be destroyed since the AtencionEspecializada " + atencionEspecializadaCollectionOrphanCheckAtencionEspecializada + " in its atencionEspecializadaCollection field has a non-nullable municipio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento departamento = municipio.getDepartamento();
            if (departamento != null) {
                departamento.getMunicipioCollection().remove(municipio);
                departamento = em.merge(departamento);
            }
            em.remove(municipio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Municipio> findMunicipioEntities() {
        return findMunicipioEntities(true, -1, -1);
    }

    public List<Municipio> findMunicipioEntities(int maxResults, int firstResult) {
        return findMunicipioEntities(false, maxResults, firstResult);
    }

    private List<Municipio> findMunicipioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Municipio.class));
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

    public Municipio findMunicipio(MunicipioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Municipio.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Municipio> rt = cq.from(Municipio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
