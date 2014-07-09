package com.javiermoreno.springboot.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author ciberado
 */
@Entity
public class Persona implements Serializable {
    
    private static final long serialVersionUID = 0L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String docIdentificacion;
    
    private String nombre;

    private String apellidos;
    
    public Persona() {
    }

    public Persona(String docIdentificacion, String nombre, String apellidos) {
        this.docIdentificacion = docIdentificacion;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }
  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocIdentificacion() {
        return docIdentificacion;
    }

    public void setDocIdentificacion(String docIdentificacion) {
        this.docIdentificacion = docIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.docIdentificacion != null ? this.docIdentificacion.hashCode() : 0);
        hash = 89 * hash + (this.apellidos != null ? this.apellidos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Persona other = (Persona) obj;
        if ((this.docIdentificacion == null) ? (other.docIdentificacion != null) : !this.docIdentificacion.equals(other.docIdentificacion)) {
            return false;
        }
        if ((this.apellidos == null) ? (other.apellidos != null) : !this.apellidos.equals(other.apellidos)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", docIdentificacion=" + docIdentificacion + ", nombre=" + nombre + ", apellidos=" + apellidos + '}';
    }

    
    
}
