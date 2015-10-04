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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.lasmedilas.siates.jpa.controler.exceptions.IllegalOrphanException;
import org.lasmedilas.siates.jpa.controler.exceptions.NonexistentEntityException;
import org.lasmedilas.siates.jpa.controler.exceptions.PreexistingEntityException;
import org.lasmedilas.siates.jpa.entidades.Departamento;

/**
 *
 * @author Franco
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) throws PreexistingEntityException, Exception {
        if (departamento.getMunicipioCollection() == null) {
            departamento.setMunicipioCollection(new ArrayList<Municipio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Municipio> attachedMunicipioCollection = new ArrayList<Municipio>();
            for (Municipio municipioCollectionMunicipioToAttach : departamento.getMunicipioCollection()) {
                municipioCollectionMunicipioToAttach = em.getReference(municipioCollectionMunicipioToAttach.getClass(), municipioCollectionMunicipioToAttach.getMunicipioPK());
                attachedMunicipioCollection.add(municipioCollectionMunicipioToAttach);
            }
            departamento.setMunicipioCollection(attachedMunicipioCollection);
            em.persist(departamento);
            for (Municipio municipioCollectionMunicipio : departamento.getMunicipioCollection()) {
                Departamento oldDepartamentoOfMunicipioCollectionMunicipio = municipioCollectionMunicipio.getDepartamento();
                municipioCollectionMunicipio.setDepartamento(departamento);
                municipioCollectionMunicipio = em.merge(municipioCollectionMunicipio);
                if (oldDepartamentoOfMunicipioCollectionMunicipio != null) {
                    oldDepartamentoOfMunicipioCollectionMunicipio.getMunicipioCollection().remove(municipioCollectionMunicipio);
                    oldDepartamentoOfMunicipioCollectionMunicipio = em.merge(oldDepartamentoOfMunicipioCollectionMunicipio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDepartamento(departamento.getIdDepartamento()) != null) {
                throw new PreexistingEntityException("Departamento " + departamento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepartamento());
            Collection<Municipio> municipioCollectionOld = persistentDepartamento.getMunicipioCollection();
            Collection<Municipio> municipioCollectionNew = departamento.getMunicipioCollection();
            List<String> illegalOrphanMessages = null;
            for (Municipio municipioCollectionOldMunicipio : municipioCollectionOld) {
                if (!municipioCollectionNew.contains(municipioCollectionOldMunicipio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Municipio " + municipioCollectionOldMunicipio + " since its departamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Municipio> attachedMunicipioCollectionNew = new ArrayList<Municipio>();
            for (Municipio municipioCollectionNewMunicipioToAttach : municipioCollectionNew) {
                municipioCollectionNewMunicipioToAttach = em.getReference(municipioCollectionNewMunicipioToAttach.getClass(), municipioCollectionNewMunicipioToAttach.getMunicipioPK());
                attachedMunicipioCollectionNew.add(municipioCollectionNewMunicipioToAttach);
            }
            municipioCollectionNew = attachedMunicipioCollectionNew;
            departamento.setMunicipioCollection(municipioCollectionNew);
            departamento = em.merge(departamento);
            for (Municipio municipioCollectionNewMunicipio : municipioCollectionNew) {
                if (!municipioCollectionOld.contains(municipioCollectionNewMunicipio)) {
                    Departamento oldDepartamentoOfMunicipioCollectionNewMunicipio = municipioCollectionNewMunicipio.getDepartamento();
                    municipioCollectionNewMunicipio.setDepartamento(departamento);
                    municipioCollectionNewMunicipio = em.merge(municipioCollectionNewMunicipio);
                    if (oldDepartamentoOfMunicipioCollectionNewMunicipio != null && !oldDepartamentoOfMunicipioCollectionNewMunicipio.equals(departamento)) {
                        oldDepartamentoOfMunicipioCollectionNewMunicipio.getMunicipioCollection().remove(municipioCollectionNewMunicipio);
                        oldDepartamentoOfMunicipioCollectionNewMunicipio = em.merge(oldDepartamentoOfMunicipioCollectionNewMunicipio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getIdDepartamento();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepartamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Municipio> municipioCollectionOrphanCheck = departamento.getMunicipioCollection();
            for (Municipio municipioCollectionOrphanCheckMunicipio : municipioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Municipio " + municipioCollectionOrphanCheckMunicipio + " in its municipioCollection field has a non-nullable departamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
