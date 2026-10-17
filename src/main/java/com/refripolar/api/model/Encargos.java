package com.refripolar.api.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "encargos")
@NamedQueries({
    @NamedQuery(name = "Encargos.findAll", query = "SELECT e FROM Encargos e"),
    @NamedQuery(name = "Encargos.findById", query = "SELECT e FROM Encargos e WHERE e.id = :id"),
    @NamedQuery(name = "Encargos.findByTipo", query = "SELECT e FROM Encargos e WHERE e.tipo = :tipo"),
    @NamedQuery(name = "Encargos.findByNombre", query = "SELECT e FROM Encargos e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Encargos.findByTerminada", query = "SELECT e FROM Encargos e WHERE e.terminada = :terminada")})
public class Encargos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "terminada")
    private Boolean terminada;
    @Column(name = "prioridad")
    private Integer prioridad;
    @Column(name = "porcentaje")
    private Integer porcentaje;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Clientes idCliente;
    @JoinColumn(name = "id_encargado", referencedColumnName = "id")
    @ManyToOne
    private Empleados idEncargado;
    @OneToMany(mappedBy = "idAsignado")
    private Collection<Empleados> empleadosCollection;

    public Encargos() {
    }

    public Encargos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getTerminada() {
        return terminada;
    }

    public void setTerminada(Boolean terminada) {
        this.terminada = terminada;
    }

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
    }

    public Empleados getIdEncargado() {
        return idEncargado;
    }

    public void setIdEncargado(Empleados idEncargado) {
        this.idEncargado = idEncargado;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encargos)) {
            return false;
        }
        Encargos other = (Encargos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Encargos[ id=" + id + " ]";
    }
    
}
