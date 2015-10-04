/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Franco Eder Chacon Linares
 * @since  04/10/2015
 * 
 */
@Entity
@Table(name = "tipo_agresor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAgresor.findAll", query = "SELECT t FROM TipoAgresor t"),
    @NamedQuery(name = "TipoAgresor.findByIdTipoAgresor", query = "SELECT t FROM TipoAgresor t WHERE t.idTipoAgresor = :idTipoAgresor"),
    @NamedQuery(name = "TipoAgresor.findByDescripcion", query = "SELECT t FROM TipoAgresor t WHERE t.descripcion = :descripcion")})
public class TipoAgresor extends Entidad {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_agresor")
    private Integer idTipoAgresor;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToMany(mappedBy = "tipoAgresorCollection")
    private Collection<Persona> personaCollection;

    public TipoAgresor() {
    }

    public TipoAgresor(Integer idTipoAgresor) {
        this.idTipoAgresor = idTipoAgresor;
    }

    public TipoAgresor(Integer idTipoAgresor, String descripcion) {
        this.idTipoAgresor = idTipoAgresor;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoAgresor() {
        return idTipoAgresor;
    }

    public void setIdTipoAgresor(Integer idTipoAgresor) {
        this.idTipoAgresor = idTipoAgresor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Persona> getPersonaCollection() {
        return personaCollection;
    }

    public void setPersonaCollection(Collection<Persona> personaCollection) {
        this.personaCollection = personaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoAgresor != null ? idTipoAgresor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAgresor)) {
            return false;
        }
        TipoAgresor other = (TipoAgresor) object;
        if ((this.idTipoAgresor == null && other.idTipoAgresor != null) || (this.idTipoAgresor != null && !this.idTipoAgresor.equals(other.idTipoAgresor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.TipoAgresor[ idTipoAgresor=" + idTipoAgresor + " ]";
    }
    
}
