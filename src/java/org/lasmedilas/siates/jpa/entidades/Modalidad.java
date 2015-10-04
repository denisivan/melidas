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
@Table(name = "modalidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modalidad.findAll", query = "SELECT m FROM Modalidad m"),
    @NamedQuery(name = "Modalidad.findByIdModalidad", query = "SELECT m FROM Modalidad m WHERE m.idModalidad = :idModalidad"),
    @NamedQuery(name = "Modalidad.findByNombre", query = "SELECT m FROM Modalidad m WHERE m.nombre = :nombre")})
public class Modalidad extends Entidad {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_modalidad")
    private Integer idModalidad;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidad")
    private Collection<Asesoria> asesoriaCollection;

    public Modalidad() {
    }

    public Modalidad(Integer idModalidad) {
        this.idModalidad = idModalidad;
    }

    public Modalidad(Integer idModalidad, String nombre) {
        this.idModalidad = idModalidad;
        this.nombre = nombre;
    }

    public Integer getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(Integer idModalidad) {
        this.idModalidad = idModalidad;
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
        hash += (idModalidad != null ? idModalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modalidad)) {
            return false;
        }
        Modalidad other = (Modalidad) object;
        if ((this.idModalidad == null && other.idModalidad != null) || (this.idModalidad != null && !this.idModalidad.equals(other.idModalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.Modalidad[ idModalidad=" + idModalidad + " ]";
    }
    
}
