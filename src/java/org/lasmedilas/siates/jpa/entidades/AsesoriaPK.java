/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.entidades;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Franco Eder Chacon Linares
 * @since  04/10/2015
 * 
 */
@Embeddable
public class AsesoriaPK extends Entidad {
    @Basic(optional = false)
    @Column(name = "id_asesoria")
    private int idAsesoria;
    @Basic(optional = false)
    @Column(name = "id_tipo_asesoria")
    private int idTipoAsesoria;
    @Basic(optional = false)
    @Column(name = "id_modalidad")
    private int idModalidad;
    @Basic(optional = false)
    @Column(name = "id_atencion_especializada")
    private int idAtencionEspecializada;

    public AsesoriaPK() {
    }

    public AsesoriaPK(int idAsesoria, int idTipoAsesoria, int idModalidad, int idAtencionEspecializada) {
        this.idAsesoria = idAsesoria;
        this.idTipoAsesoria = idTipoAsesoria;
        this.idModalidad = idModalidad;
        this.idAtencionEspecializada = idAtencionEspecializada;
    }

    public int getIdAsesoria() {
        return idAsesoria;
    }

    public void setIdAsesoria(int idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public int getIdTipoAsesoria() {
        return idTipoAsesoria;
    }

    public void setIdTipoAsesoria(int idTipoAsesoria) {
        this.idTipoAsesoria = idTipoAsesoria;
    }

    public int getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }

    public int getIdAtencionEspecializada() {
        return idAtencionEspecializada;
    }

    public void setIdAtencionEspecializada(int idAtencionEspecializada) {
        this.idAtencionEspecializada = idAtencionEspecializada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAsesoria;
        hash += (int) idTipoAsesoria;
        hash += (int) idModalidad;
        hash += (int) idAtencionEspecializada;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsesoriaPK)) {
            return false;
        }
        AsesoriaPK other = (AsesoriaPK) object;
        if (this.idAsesoria != other.idAsesoria) {
            return false;
        }
        if (this.idTipoAsesoria != other.idTipoAsesoria) {
            return false;
        }
        if (this.idModalidad != other.idModalidad) {
            return false;
        }
        if (this.idAtencionEspecializada != other.idAtencionEspecializada) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.AsesoriaPK[ idAsesoria=" + idAsesoria + ", idTipoAsesoria=" + idTipoAsesoria + ", idModalidad=" + idModalidad + ", idAtencionEspecializada=" + idAtencionEspecializada + " ]";
    }
    
}
