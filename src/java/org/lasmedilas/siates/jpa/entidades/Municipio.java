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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "municipio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Municipio.findAll", query = "SELECT m FROM Municipio m"),
    @NamedQuery(name = "Municipio.findByIdMunicipio", query = "SELECT m FROM Municipio m WHERE m.municipioPK.idMunicipio = :idMunicipio"),
    @NamedQuery(name = "Municipio.findByIdDepartamento", query = "SELECT m FROM Municipio m WHERE m.municipioPK.idDepartamento = :idDepartamento"),
    @NamedQuery(name = "Municipio.findByNombre", query = "SELECT m FROM Municipio m WHERE m.nombre = :nombre")})
public class Municipio extends Entidad {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MunicipioPK municipioPK;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio")
    private Collection<Persona> personaCollection;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Departamento departamento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio")
    private Collection<AtencionEspecializada> atencionEspecializadaCollection;

    public Municipio() {
    }

    public Municipio(MunicipioPK municipioPK) {
        this.municipioPK = municipioPK;
    }

    public Municipio(MunicipioPK municipioPK, String nombre) {
        this.municipioPK = municipioPK;
        this.nombre = nombre;
    }

    public Municipio(int idMunicipio, int idDepartamento) {
        this.municipioPK = new MunicipioPK(idMunicipio, idDepartamento);
    }

    public MunicipioPK getMunicipioPK() {
        return municipioPK;
    }

    public void setMunicipioPK(MunicipioPK municipioPK) {
        this.municipioPK = municipioPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Persona> getPersonaCollection() {
        return personaCollection;
    }

    public void setPersonaCollection(Collection<Persona> personaCollection) {
        this.personaCollection = personaCollection;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @XmlTransient
    public Collection<AtencionEspecializada> getAtencionEspecializadaCollection() {
        return atencionEspecializadaCollection;
    }

    public void setAtencionEspecializadaCollection(Collection<AtencionEspecializada> atencionEspecializadaCollection) {
        this.atencionEspecializadaCollection = atencionEspecializadaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (municipioPK != null ? municipioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Municipio)) {
            return false;
        }
        Municipio other = (Municipio) object;
        if ((this.municipioPK == null && other.municipioPK != null) || (this.municipioPK != null && !this.municipioPK.equals(other.municipioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.Municipio[ municipioPK=" + municipioPK + " ]";
    }
    
}
