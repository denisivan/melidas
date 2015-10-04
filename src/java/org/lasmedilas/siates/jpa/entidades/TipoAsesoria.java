/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "tipo_asesoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAsesoria.findAll", query = "SELECT t FROM TipoAsesoria t"),
    @NamedQuery(name = "TipoAsesoria.findByIdTipoAsesoria", query = "SELECT t FROM TipoAsesoria t WHERE t.idTipoAsesoria = :idTipoAsesoria"),
    @NamedQuery(name = "TipoAsesoria.findByNombre", query = "SELECT t FROM TipoAsesoria t WHERE t.nombre = :nombre")})
public class TipoAsesoria extends Entidad {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_asesoria")
    private Integer idTipoAsesoria;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAsesoria")
    private Collection<Asesoria> asesoriaCollection;

    public TipoAsesoria() {
    }

    public TipoAsesoria(Integer idTipoAsesoria) {
        this.idTipoAsesoria = idTipoAsesoria;
    }

    public TipoAsesoria(Integer idTipoAsesoria, String nombre) {
        this.idTipoAsesoria = idTipoAsesoria;
        this.nombre = nombre;
    }

    public Integer getIdTipoAsesoria() {
        return idTipoAsesoria;
    }

    public void setIdTipoAsesoria(Integer idTipoAsesoria) {
        this.idTipoAsesoria = idTipoAsesoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Asesoria> getAsesoriaCollection() {
        return asesoriaCollection;
    }

    public void setAsesoriaCollection(Collection<Asesoria> asesoriaCollection) {
        this.asesoriaCollection = asesoriaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoAsesoria != null ? idTipoAsesoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAsesoria)) {
            return false;
        }
        TipoAsesoria other = (TipoAsesoria) object;
        if ((this.idTipoAsesoria == null && other.idTipoAsesoria != null) || (this.idTipoAsesoria != null && !this.idTipoAsesoria.equals(other.idTipoAsesoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.TipoAsesoria[ idTipoAsesoria=" + idTipoAsesoria + " ]";
    }
    
}
