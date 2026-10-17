package com.refripolar.api.model;

import jakarta.json.bind.annotation.JsonbTransient;
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
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "empleados")
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e"),
    @NamedQuery(name = "Empleados.findById", query = "SELECT e FROM Empleados e WHERE e.id = :id"),
    @NamedQuery(name = "Empleados.findByNombre", query = "SELECT e FROM Empleados e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleados.findByApellido1", query = "SELECT e FROM Empleados e WHERE e.apellido1 = :apellido1"),
    @NamedQuery(name = "Empleados.findByApellido2", query = "SELECT e FROM Empleados e WHERE e.apellido2 = :apellido2"),
    @NamedQuery(name = "Empleados.findByAntiguedad", query = "SELECT e FROM Empleados e WHERE e.antiguedad = :antiguedad"),
    @NamedQuery(name = "Empleados.findByReconocimientoMedico", query = "SELECT e FROM Empleados e WHERE e.reconocimientoMedico = :reconocimientoMedico")})
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido_1")
    private String apellido1;
    @Column(name = "apellido_2")
    private String apellido2;
    @Column(name = "antiguedad")
    private Integer antiguedad;
    @Column(name = "reconocimiento_medico")
    private Boolean reconocimientoMedico;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "cp")
    private Integer cp;
    @Column(name = "mail")
    private String mail;
    @Column(name = "ciudad")
    private String ciudad;
    @Column(name = "provincia")
    private String provincia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salario")
    private BigDecimal salario;
    @Column(name = "telefono")
    private Integer telefono;
    @OneToMany(mappedBy = "idEncargado")
    @JsonbTransient
    private Collection<Encargos> encargosCollection;
    @JoinColumn(name = "cod_categoriaProfesional", referencedColumnName = "codigo")
    @ManyToOne
    private CategoriasProfesionales codcategoriaProfesional;
    @JoinColumn(name = "id_asignado", referencedColumnName = "id")
    @ManyToOne
    @JsonbTransient
    private Encargos idAsignado;
    @Column(name = "imagen")
    private String imagen;

    public Empleados() {
    }

    public Empleados(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCp() {
        return cp;
    }

    public void setCp(Integer cp) {
        this.cp = cp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Integer getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Boolean getReconocimientoMedico() {
        return reconocimientoMedico;
    }

    public void setReconocimientoMedico(Boolean reconocimientoMedico) {
        this.reconocimientoMedico = reconocimientoMedico;
    }

    public Collection<Encargos> getEncargosCollection() {
        return encargosCollection;
    }

    public void setEncargosCollection(Collection<Encargos> encargosCollection) {
        this.encargosCollection = encargosCollection;
    }

    public CategoriasProfesionales getCodcategoriaProfesional() {
        return codcategoriaProfesional;
    }

    public void setCodcategoriaProfesional(CategoriasProfesionales codcategoriaProfesional) {
        this.codcategoriaProfesional = codcategoriaProfesional;
    }

    public Encargos getIdAsignado() {
        return idAsignado;
    }

    public void setIdAsignado(Encargos idAsignado) {
        this.idAsignado = idAsignado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Empleados[ id=" + id + " ]";
    }

}
