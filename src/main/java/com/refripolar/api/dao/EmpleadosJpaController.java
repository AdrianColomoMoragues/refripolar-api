package com.refripolar.api.dao;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.refripolar.api.model.CategoriasProfesionales;
import com.refripolar.api.model.Empleados;
import com.refripolar.api.model.Encargos;
import com.refripolar.api.dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class EmpleadosJpaController implements Serializable {

    public EmpleadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleados empleados) {
        if (empleados.getEncargosCollection() == null) {
            empleados.setEncargosCollection(new ArrayList<Encargos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriasProfesionales codcategoriaProfesional = empleados.getCodcategoriaProfesional();
            if (codcategoriaProfesional != null) {
                codcategoriaProfesional = em.getReference(codcategoriaProfesional.getClass(), codcategoriaProfesional.getCodigo());
                empleados.setCodcategoriaProfesional(codcategoriaProfesional);
            }
            Collection<Encargos> attachedEncargosCollection = new ArrayList<Encargos>();
            for (Encargos encargosCollectionEncargosToAttach : empleados.getEncargosCollection()) {
                encargosCollectionEncargosToAttach = em.getReference(encargosCollectionEncargosToAttach.getClass(), encargosCollectionEncargosToAttach.getId());
                attachedEncargosCollection.add(encargosCollectionEncargosToAttach);
            }
            empleados.setEncargosCollection(attachedEncargosCollection);
            em.persist(empleados);
            if (codcategoriaProfesional != null) {
                codcategoriaProfesional.getEmpleadosCollection().add(empleados);
                codcategoriaProfesional = em.merge(codcategoriaProfesional);
            }
            for (Encargos encargosCollectionEncargos : empleados.getEncargosCollection()) {
                Empleados oldIdEncargadoOfEncargosCollectionEncargos = encargosCollectionEncargos.getIdEncargado();
                encargosCollectionEncargos.setIdEncargado(empleados);
                encargosCollectionEncargos = em.merge(encargosCollectionEncargos);
                if (oldIdEncargadoOfEncargosCollectionEncargos != null) {
                    oldIdEncargadoOfEncargosCollectionEncargos.getEncargosCollection().remove(encargosCollectionEncargos);
                    oldIdEncargadoOfEncargosCollectionEncargos = em.merge(oldIdEncargadoOfEncargosCollectionEncargos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleados empleados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados persistentEmpleados = em.find(Empleados.class, empleados.getId());
            CategoriasProfesionales codcategoriaProfesionalOld = persistentEmpleados.getCodcategoriaProfesional();
            CategoriasProfesionales codcategoriaProfesionalNew = empleados.getCodcategoriaProfesional();
            Collection<Encargos> encargosCollectionOld = persistentEmpleados.getEncargosCollection();
            Collection<Encargos> encargosCollectionNew = empleados.getEncargosCollection();
            if (codcategoriaProfesionalNew != null) {
                codcategoriaProfesionalNew = em.getReference(codcategoriaProfesionalNew.getClass(), codcategoriaProfesionalNew.getCodigo());
                empleados.setCodcategoriaProfesional(codcategoriaProfesionalNew);
            }
            Collection<Encargos> attachedEncargosCollectionNew = new ArrayList<Encargos>();
            for (Encargos encargosCollectionNewEncargosToAttach : encargosCollectionNew) {
                encargosCollectionNewEncargosToAttach = em.getReference(encargosCollectionNewEncargosToAttach.getClass(), encargosCollectionNewEncargosToAttach.getId());
                attachedEncargosCollectionNew.add(encargosCollectionNewEncargosToAttach);
            }
            encargosCollectionNew = attachedEncargosCollectionNew;
            empleados.setEncargosCollection(encargosCollectionNew);
            empleados = em.merge(empleados);
            if (codcategoriaProfesionalOld != null && !codcategoriaProfesionalOld.equals(codcategoriaProfesionalNew)) {
                codcategoriaProfesionalOld.getEmpleadosCollection().remove(empleados);
                codcategoriaProfesionalOld = em.merge(codcategoriaProfesionalOld);
            }
            if (codcategoriaProfesionalNew != null && !codcategoriaProfesionalNew.equals(codcategoriaProfesionalOld)) {
                codcategoriaProfesionalNew.getEmpleadosCollection().add(empleados);
                codcategoriaProfesionalNew = em.merge(codcategoriaProfesionalNew);
            }
            for (Encargos encargosCollectionOldEncargos : encargosCollectionOld) {
                if (!encargosCollectionNew.contains(encargosCollectionOldEncargos)) {
                    encargosCollectionOldEncargos.setIdEncargado(null);
                    encargosCollectionOldEncargos = em.merge(encargosCollectionOldEncargos);
                }
            }
            for (Encargos encargosCollectionNewEncargos : encargosCollectionNew) {
                if (!encargosCollectionOld.contains(encargosCollectionNewEncargos)) {
                    Empleados oldIdEncargadoOfEncargosCollectionNewEncargos = encargosCollectionNewEncargos.getIdEncargado();
                    encargosCollectionNewEncargos.setIdEncargado(empleados);
                    encargosCollectionNewEncargos = em.merge(encargosCollectionNewEncargos);
                    if (oldIdEncargadoOfEncargosCollectionNewEncargos != null && !oldIdEncargadoOfEncargosCollectionNewEncargos.equals(empleados)) {
                        oldIdEncargadoOfEncargosCollectionNewEncargos.getEncargosCollection().remove(encargosCollectionNewEncargos);
                        oldIdEncargadoOfEncargosCollectionNewEncargos = em.merge(oldIdEncargadoOfEncargosCollectionNewEncargos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleados.getId();
                if (findEmpleados(id) == null) {
                    throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados empleados;
            try {
                empleados = em.getReference(Empleados.class, id);
                empleados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.", enfe);
            }
            CategoriasProfesionales codcategoriaProfesional = empleados.getCodcategoriaProfesional();
            if (codcategoriaProfesional != null) {
                codcategoriaProfesional.getEmpleadosCollection().remove(empleados);
                codcategoriaProfesional = em.merge(codcategoriaProfesional);
            }
            Collection<Encargos> encargosCollection = empleados.getEncargosCollection();
            for (Encargos encargosCollectionEncargos : encargosCollection) {
                encargosCollectionEncargos.setIdEncargado(null);
                encargosCollectionEncargos = em.merge(encargosCollectionEncargos);
            }
            em.remove(empleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleados> findEmpleadosEntities() {
        return findEmpleadosEntities(true, -1, -1);
    }

    public List<Empleados> findEmpleadosEntities(int maxResults, int firstResult) {
        return findEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<Empleados> findEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleados.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Empleados findEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleados> rt = cq.from(Empleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
