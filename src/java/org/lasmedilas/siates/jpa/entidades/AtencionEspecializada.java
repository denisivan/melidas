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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
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
@Table(name = "atencion_especializada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtencionEspecializada.findAll", query = "SELECT a FROM AtencionEspecializada a"),
    @NamedQuery(name = "AtencionEspecializada.findByIdAtencionEspecializada", query = "SELECT a FROM AtencionEspecializada a WHERE a.idAtencionEspecializada = :idAtencionEspecializada"),
    @NamedQuery(name = "AtencionEspecializada.findByEdadAfectada", query = "SELECT a FROM AtencionEspecializada a WHERE a.edadAfectada = :edadAfectada"),
    @NamedQuery(name = "AtencionEspecializada.findByMetodoReferencia", query = "SELECT a FROM AtencionEspecializada a WHERE a.metodoReferencia = :metodoReferencia"),
    @NamedQuery(name = "AtencionEspecializada.findByArea", query = "SELECT a FROM AtencionEspecializada a WHERE a.area = :area"),
    @NamedQuery(name = "AtencionEspecializada.findByCasoFavorable", query = "SELECT a FROM AtencionEspecializada a WHERE a.casoFavorable = :casoFavorable"),
    @NamedQuery(name = "AtencionEspecializada.findByDiagnostico", query = "SELECT a FROM AtencionEspecializada a WHERE a.diagnostico = :diagnostico")})
public class AtencionEspecializada extends Entidad {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_atencion_especializada")
    private Integer idAtencionEspecializada;
    @Basic(optional = false)
    @Column(name = "edad_afectada")
    private int edadAfectada;
    @Basic(optional = false)
    @Column(name = "metodo_referencia")
    private String metodoReferencia;
    @Basic(optional = false)
    @Column(name = "area")
    private String area;
    @Basic(optional = false)
    @Column(name = "caso_favorable")
    private boolean casoFavorable;
    @Basic(optional = false)
    @Column(name = "diagnostico")
    private String diagnostico;
    @JoinTable(name = "atencion_especializada_tipo_violencia", joinColumns = {
        @JoinColumn(name = "id_atencion_especializada", referencedColumnName = "id_atencion_especializada")}, inverseJoinColumns = {
        @JoinColumn(name = "id_tipo_violencia", referencedColumnName = "id_tipo_violencia")})
    @ManyToMany
    private Collection<TipoViolencia> tipoViolenciaCollection;
    @JoinTable(name = "atencion_especializada_persona", joinColumns = {
        @JoinColumn(name = "id_atencion_especializada", referencedColumnName = "id_atencion_especializada")}, inverseJoinColumns = {
        @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")})
    @ManyToMany
    private Collection<Persona> personaCollection;
    @JoinTable(name = "acompanhamento", joinColumns = {
        @JoinColumn(name = "id_atencion_especializada", referencedColumnName = "id_atencion_especializada")}, inverseJoinColumns = {
        @JoinColumn(name = "id_institucion", referencedColumnName = "id_institucion")})
    @ManyToMany
    private Collection<Institucion> institucionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atencionEspecializada")
    private Collection<Asesoria> asesoriaCollection;
    @JoinColumns({
        @JoinColumn(name = "id_municipio", referencedColumnName = "id_municipio"),
        @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento")})
    @ManyToOne(optional = false)
    private Municipio municipio;

    public AtencionEspecializada() {
    }

    public AtencionEspecializada(Integer idAtencionEspecializada) {
        this.idAtencionEspecializada = idAtencionEspecializada;
    }

    public AtencionEspecializada(Integer idAtencionEspecializada, int edadAfectada, String metodoReferencia, String area, boolean casoFavorable, String diagnostico) {
        this.idAtencionEspecializada = idAtencionEspecializada;
        this.edadAfectada = edadAfectada;
        this.metodoReferencia = metodoReferencia;
        this.area = area;
        this.casoFavorable = casoFavorable;
        this.diagnostico = diagnostico;
    }

    public Integer getIdAtencionEspecializada() {
        return idAtencionEspecializada;
    }

    public void setIdAtencionEspecializada(Integer idAtencionEspecializada) {
        this.idAtencionEspecializada = idAtencionEspecializada;
    }

    public int getEdadAfectada() {
        return edadAfectada;
    }

    public void setEdadAfectada(int edadAfectada) {
        this.edadAfectada = edadAfectada;
    }

    public String getMetodoReferencia() {
        return metodoReferencia;
    }

    public void setMetodoReferencia(String metodoReferencia) {
        this.metodoReferencia = metodoReferencia;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean getCasoFavorable() {
        return casoFavorable;
    }

    public void setCasoFavorable(boolean casoFavorable) {
        this.casoFavorable = casoFavorable;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    @XmlTransient
    public Collection<TipoViolencia> getTipoViolenciaCollection() {
        return tipoViolenciaCollection;
    }

    public void setTipoViolenciaCollection(Collection<TipoViolencia> tipoViolenciaCollection) {
        this.tipoViolenciaCollection = tipoViolenciaCollection;
    }

    @XmlTransient
    public Collection<Persona> getPersonaCollection() {
        return personaCollection;
    }

    public void setPersonaCollection(Collection<Persona> personaCollection) {
        this.personaCollection = personaCollection;
    }

    @XmlTransient
    public Collection<Institucion> getInstitucionCollection() {
        return institucionCollection;
    }

    public void setInstitucionCollection(Collection<Institucion> institucionCollection) {
        this.institucionCollection = institucionCollection;
    }

    @XmlTransient
    public Collection<Asesoria> getAsesoriaCollection() {
        return asesoriaCollection;
    }

    public void setAsesoriaCollection(Collection<Asesoria> asesoriaCollection) {
        this.asesoriaCollection = asesoriaCollection;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAtencionEspecializada != null ? idAtencionEspecializada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtencionEspecializada)) {
            return false;
        }
        AtencionEspecializada other = (AtencionEspecializada) object;
        if ((this.idAtencionEspecializada == null && other.idAtencionEspecializada != null) || (this.idAtencionEspecializada != null && !this.idAtencionEspecializada.equals(other.idAtencionEspecializada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.AtencionEspecializada[ idAtencionEspecializada=" + idAtencionEspecializada + " ]";
    }
    
}
