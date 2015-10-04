/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lasmedilas.siates.jpa.entidades;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Franco Eder Chacon Linares
 * @since  04/10/2015
 * 
 */
@Entity
@Table(name = "asesoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asesoria.findAll", query = "SELECT a FROM Asesoria a"),
    @NamedQuery(name = "Asesoria.findByIdAsesoria", query = "SELECT a FROM Asesoria a WHERE a.asesoriaPK.idAsesoria = :idAsesoria"),
    @NamedQuery(name = "Asesoria.findByIdTipoAsesoria", query = "SELECT a FROM Asesoria a WHERE a.asesoriaPK.idTipoAsesoria = :idTipoAsesoria"),
    @NamedQuery(name = "Asesoria.findByIdModalidad", query = "SELECT a FROM Asesoria a WHERE a.asesoriaPK.idModalidad = :idModalidad"),
    @NamedQuery(name = "Asesoria.findByIdAtencionEspecializada", query = "SELECT a FROM Asesoria a WHERE a.asesoriaPK.idAtencionEspecializada = :idAtencionEspecializada")})
public class Asesoria extends Entidad{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AsesoriaPK asesoriaPK;
    @JoinColumn(name = "id_atencion_especializada", referencedColumnName = "id_atencion_especializada", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AtencionEspecializada atencionEspecializada;
    @JoinColumn(name = "id_modalidad", referencedColumnName = "id_modalidad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Modalidad modalidad;
    @JoinColumn(name = "id_tipo_asesoria", referencedColumnName = "id_tipo_asesoria", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoAsesoria tipoAsesoria;

    public Asesoria() {
    }

    public Asesoria(AsesoriaPK asesoriaPK) {
        this.asesoriaPK = asesoriaPK;
    }

    public Asesoria(int idAsesoria, int idTipoAsesoria, int idModalidad, int idAtencionEspecializada) {
        this.asesoriaPK = new AsesoriaPK(idAsesoria, idTipoAsesoria, idModalidad, idAtencionEspecializada);
    }

    public AsesoriaPK getAsesoriaPK() {
        return asesoriaPK;
    }

    public void setAsesoriaPK(AsesoriaPK asesoriaPK) {
        this.asesoriaPK = asesoriaPK;
    }

    public AtencionEspecializada getAtencionEspecializada() {
        return atencionEspecializada;
    }

    public void setAtencionEspecializada(AtencionEspecializada atencionEspecializada) {
        this.atencionEspecializada = atencionEspecializada;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public TipoAsesoria getTipoAsesoria() {
        return tipoAsesoria;
    }

    public void setTipoAsesoria(TipoAsesoria tipoAsesoria) {
        this.tipoAsesoria = tipoAsesoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asesoriaPK != null ? asesoriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asesoria)) {
            return false;
        }
        Asesoria other = (Asesoria) object;
        if ((this.asesoriaPK == null && other.asesoriaPK != null) || (this.asesoriaPK != null && !this.asesoriaPK.equals(other.asesoriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lasmedilas.siates.Asesoria[ asesoriaPK=" + asesoriaPK + " ]";
    }
    
}
