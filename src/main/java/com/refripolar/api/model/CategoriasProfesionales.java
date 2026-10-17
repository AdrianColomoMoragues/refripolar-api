package com.refripolar.api.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "categorias_profesionales")
@NamedQueries({
    @NamedQuery(name = "CategoriasProfesionales.findAll", query = "SELECT c FROM CategoriasProfesionales c"),
    @NamedQuery(name = "CategoriasProfesionales.findByCodigo", query = "SELECT c FROM CategoriasProfesionales c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CategoriasProfesionales.findByDescripcion", query = "SELECT c FROM CategoriasProfesionales c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CategoriasProfesionales.findByEncargo", query = "SELECT c FROM CategoriasProfesionales c WHERE c.encargo = :encargo")})
public class CategoriasProfesionales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "encargo")
    private String encargo;
    @OneToMany(mappedBy = "codcategoriaProfesional")
    @JsonbTransient
    private Collection<Empleados> empleadosCollection;

    public CategoriasProfesionales() {
    }

    public CategoriasProfesionales(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEncargo() {
        return encargo;
    }

    public void setEncargo(String encargo) {
        this.encargo = encargo;
    }

    public Collection<Empleados> getEmpleadosCollection() {
        return empleadosCollection;
    }

    public void setEmpleadosCollection(Collection<Empleados> empleadosCollection) {
        this.empleadosCollection = empleadosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriasProfesionales)) {
            return false;
        }
        CategoriasProfesionales other = (CategoriasProfesionales) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CategoriasProfesionales[ codigo=" + codigo + " ]";
    }
    
}
