/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Franco Eder Chacon Linares
 * @since  04/10/2015
 * 
 */
@Entity
@Table(name = "persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findByIdPersona", query = "SELECT p FROM Persona p WHERE p.idPersona = :idPersona"),
    @NamedQuery(name = "Persona.findByNombre", query = "SELECT p FROM Persona p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Persona.findByTipo", query = "SELECT p FROM Persona p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Persona.findByFechaNacimiento", query = "SELECT p FROM Persona p WHERE p.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Persona.findByNivelAcademico", query = "SELECT p FROM Persona p WHERE p.nivelAcademico = :nivelAcademico"),
    @NamedQuery(name = "Persona.findByTelefono", query = "SELECT p FROM Persona p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Persona.findBySexo", query = "SELECT p FROM Persona p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "Persona.findByDireccion", query = "SELECT p FROM Persona p WHERE p.direccion = :direccion")})
public class Persona extends Entidad {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_persona")
    private Integer idPersona;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @Column(name = "nivel_academico")
    private String nivelAcademico;
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @ManyToMany(mappedBy = "personaCollection")
    private Collection<AtencionEspecializada> atencionEspecializadaCollection;
    @JoinTable(name = "referencia_persona", joinColumns = {
        @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")}, inverseJoinColumns = {
        @JoinColumn(name = "id_referencia", referencedColumnName = "id_referencia")})
    @ManyToMany
    private Collection<Referencia> referenciaCollection;
    @JoinTable(name = "tipo_agresor_persona", joinColumns = {
        @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")}, inverseJoinColumns = {
        @JoinColumn(name = "id_tipo_agresor", referencedColumnName = "id_tipo_agresor")})
    @ManyToMany
    private Collection<TipoAgresor> tipoAgresorCollection;
    @ManyToMany(mappedBy = "personaCollection")
    private Collection<Profesion> profesionCollection;
    @JoinColumns({
        @JoinColumn(name = "id_municipio", referencedColumnName = "id_municipio"),
        @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento")})
    @ManyToOne(optional = false)
    private Municipio municipio;

    public Persona() {
    }

    public Persona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public Persona(Integer idPersona, String nombre, String tipo, Date fechaNacimiento, String nivelAcademico, String telefono, String sexo, String direccion) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaNacimiento = fechaNacimiento;
        this.nivelAcademico = nivelAcademico;
        this.telefono = telefono;
        this.sexo = sexo;
        this.direccion = direccion;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(String nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @XmlTransient
    public Collection<AtencionEspecializada> getAtencionEspecializadaCollection() {
        return atencionEspecializadaCollection;
    }

    public void setAtencionEspecializadaCollection(Collection<AtencionEspecializada> atencionEspecializadaCollection) {
        this.atencionEspecializadaCollection = atencionEspecializadaCollection;
    }

    @XmlTransient
    public Collection<Referencia> getReferenciaCollection() {
        return referenciaCollection;
    }

    public void setReferenciaCollection(Collection<Referencia> referenciaCollection) {
        this.referenciaCollection = referenciaCollection;
    }

    @XmlTransient
    public Collection<TipoAgresor> getTipoAgresorCollection() {
        return tipoAgresorCollection;
    }

    public void setTipoAgresorCollection(Collection<TipoAgresor> tipoAgresorCollection) {
        this.tipoAgresorCollection = tipoAgresorCollection;
    }

    @XmlTransient
    public Collection<Profesion> getProfesionCollection() {
        return profesionCollection;
    }

    public void setProfesionCollection(Collection<Profesion> profesionCollection) {
        this.profesionCollection = profesionCollection;
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
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.Persona[ idPersona=" + idPersona + " ]";
    }
    
}
