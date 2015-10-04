/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.entidades;

import java.io.Serializable;
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
public class MunicipioPK extends Entidad {
    @Basic(optional = false)
    @Column(name = "id_municipio")
    private int idMunicipio;
    @Basic(optional = false)
    @Column(name = "id_departamento")
    private int idDepartamento;

    public MunicipioPK() {
    }

    public MunicipioPK(int idMunicipio, int idDepartamento) {
        this.idMunicipio = idMunicipio;
        this.idDepartamento = idDepartamento;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idMunicipio;
        hash += (int) idDepartamento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipioPK)) {
            return false;
        }
        MunicipioPK other = (MunicipioPK) object;
        if (this.idMunicipio != other.idMunicipio) {
            return false;
        }
        if (this.idDepartamento != other.idDepartamento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.MunicipioPK[ idMunicipio=" + idMunicipio + ", idDepartamento=" + idDepartamento + " ]";
    }
    
}
