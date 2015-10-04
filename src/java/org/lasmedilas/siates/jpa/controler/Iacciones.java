/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package org.lasmedilas.siates.jpa.controler;

import java.io.Serializable;
import java.util.List;
import org.lasmedilas.siates.jpa.entidades.Entidad;

/**
 *
 * @author Franco Eder Chacon Linares 
 * @since 04/10/2015
 * 
 */
public interface Iacciones extends Serializable{
    
    public void guardar (Entidad entidad);
    
    public void crear (Entidad entidad);
    
    public void editar (Entidad entidad);
    
    public void borrar (Entidad entidad);
    
    public List<?> getEntidades ();
    
    public List<?> getEntidades (String comodin);
    
    public Entidad getEntidad(String llave);
    
    public Entidad getEntidad(Entidad llave);
    
}
