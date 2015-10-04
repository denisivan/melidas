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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "tipo_violencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoViolencia.findAll", query = "SELECT t FROM TipoViolencia t"),
    @NamedQuery(name = "TipoViolencia.findByIdTipoViolencia", query = "SELECT t FROM TipoViolencia t WHERE t.idTipoViolencia = :idTipoViolencia"),
    @NamedQuery(name = "TipoViolencia.findByNombre", query = "SELECT t FROM TipoViolencia t WHERE t.nombre = :nombre")})
public class TipoViolencia extends Entidad {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_violencia")
    private Integer idTipoViolencia;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @ManyToMany(mappedBy = "tipoViolenciaCollection")
    private Collection<AtencionEspecializada> atencionEspecializadaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padreTipoViolencia")
    private Collection<TipoViolencia> tipoViolenciaCollection;
    @JoinColumn(name = "padre_tipo_violencia", referencedColumnName = "id_tipo_violencia")
    @ManyToOne(optional = false)
    private TipoViolencia padreTipoViolencia;

    public TipoViolencia() {
    }

    public TipoViolencia(Integer idTipoViolencia) {
        this.idTipoViolencia = idTipoViolencia;
    }

    public TipoViolencia(Integer idTipoViolencia, String nombre) {
        this.idTipoViolencia = idTipoViolencia;
        this.nombre = nombre;
    }

    public Integer getIdTipoViolencia() {
        return idTipoViolencia;
    }

    public void setIdTipoViolencia(Integer idTipoViolencia) {
        this.idTipoViolencia = idTipoViolencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<AtencionEspecializada> getAtencionEspecializadaCollection() {
        return atencionEspecializadaCollection;
    }

    public void setAtencionEspecializadaCollection(Collection<AtencionEspecializada> atencionEspecializadaCollection) {
        this.atencionEspecializadaCollection = atencionEspecializadaCollection;
    }

    @XmlTransient
    public Collection<TipoViolencia> getTipoViolenciaCollection() {
        return tipoViolenciaCollection;
    }

    public void setTipoViolenciaCollection(Collection<TipoViolencia> tipoViolenciaCollection) {
        this.tipoViolenciaCollection = tipoViolenciaCollection;
    }

    public TipoViolencia getPadreTipoViolencia() {
        return padreTipoViolencia;
    }

    public void setPadreTipoViolencia(TipoViolencia padreTipoViolencia) {
        this.padreTipoViolencia = padreTipoViolencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoViolencia != null ? idTipoViolencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoViolencia)) {
            return false;
        }
        TipoViolencia other = (TipoViolencia) object;
        if ((this.idTipoViolencia == null && other.idTipoViolencia != null) || (this.idTipoViolencia != null && !this.idTipoViolencia.equals(other.idTipoViolencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.TipoViolencia[ idTipoViolencia=" + idTipoViolencia + " ]";
    }
    
}
