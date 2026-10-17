package com.refripolar.api.dao;

import com.refripolar.api.model.CategoriasProfesionales;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.refripolar.api.model.Empleados;
import com.refripolar.api.dao.exceptions.NonexistentEntityException;
import com.refripolar.api.dao.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class CategoriasProfesionalesJpaController implements Serializable {

    public CategoriasProfesionalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoriasProfesionales categoriasProfesionales) throws PreexistingEntityException, Exception {
        if (categoriasProfesionales.getEmpleadosCollection() == null) {
            categoriasProfesionales.setEmpleadosCollection(new ArrayList<Empleados>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Empleados> attachedEmpleadosCollection = new ArrayList<Empleados>();
            for (Empleados empleadosCollectionEmpleadosToAttach : categoriasProfesionales.getEmpleadosCollection()) {
                empleadosCollectionEmpleadosToAttach = em.getReference(empleadosCollectionEmpleadosToAttach.getClass(), empleadosCollectionEmpleadosToAttach.getId());
                attachedEmpleadosCollection.add(empleadosCollectionEmpleadosToAttach);
            }
            categoriasProfesionales.setEmpleadosCollection(attachedEmpleadosCollection);
            em.persist(categoriasProfesionales);
            for (Empleados empleadosCollectionEmpleados : categoriasProfesionales.getEmpleadosCollection()) {
                CategoriasProfesionales oldCodcategoriaProfesionalOfEmpleadosCollectionEmpleados = empleadosCollectionEmpleados.getCodcategoriaProfesional();
                empleadosCollectionEmpleados.setCodcategoriaProfesional(categoriasProfesionales);
                empleadosCollectionEmpleados = em.merge(empleadosCollectionEmpleados);
                if (oldCodcategoriaProfesionalOfEmpleadosCollectionEmpleados != null) {
                    oldCodcategoriaProfesionalOfEmpleadosCollectionEmpleados.getEmpleadosCollection().remove(empleadosCollectionEmpleados);
                    oldCodcategoriaProfesionalOfEmpleadosCollectionEmpleados = em.merge(oldCodcategoriaProfesionalOfEmpleadosCollectionEmpleados);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategoriasProfesionales(categoriasProfesionales.getCodigo()) != null) {
                throw new PreexistingEntityException("CategoriasProfesionales " + categoriasProfesionales + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategoriasProfesionales categoriasProfesionales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriasProfesionales persistentCategoriasProfesionales = em.find(CategoriasProfesionales.class, categoriasProfesionales.getCodigo());
            Collection<Empleados> empleadosCollectionOld = persistentCategoriasProfesionales.getEmpleadosCollection();
            Collection<Empleados> empleadosCollectionNew = categoriasProfesionales.getEmpleadosCollection();
            Collection<Empleados> attachedEmpleadosCollectionNew = new ArrayList<Empleados>();
            for (Empleados empleadosCollectionNewEmpleadosToAttach : empleadosCollectionNew) {
                empleadosCollectionNewEmpleadosToAttach = em.getReference(empleadosCollectionNewEmpleadosToAttach.getClass(), empleadosCollectionNewEmpleadosToAttach.getId());
                attachedEmpleadosCollectionNew.add(empleadosCollectionNewEmpleadosToAttach);
            }
            empleadosCollectionNew = attachedEmpleadosCollectionNew;
            categoriasProfesionales.setEmpleadosCollection(empleadosCollectionNew);
            categoriasProfesionales = em.merge(categoriasProfesionales);
            for (Empleados empleadosCollectionOldEmpleados : empleadosCollectionOld) {
                if (!empleadosCollectionNew.contains(empleadosCollectionOldEmpleados)) {
                    empleadosCollectionOldEmpleados.setCodcategoriaProfesional(null);
                    empleadosCollectionOldEmpleados = em.merge(empleadosCollectionOldEmpleados);
                }
            }
            for (Empleados empleadosCollectionNewEmpleados : empleadosCollectionNew) {
                if (!empleadosCollectionOld.contains(empleadosCollectionNewEmpleados)) {
                    CategoriasProfesionales oldCodcategoriaProfesionalOfEmpleadosCollectionNewEmpleados = empleadosCollectionNewEmpleados.getCodcategoriaProfesional();
                    empleadosCollectionNewEmpleados.setCodcategoriaProfesional(categoriasProfesionales);
                    empleadosCollectionNewEmpleados = em.merge(empleadosCollectionNewEmpleados);
                    if (oldCodcategoriaProfesionalOfEmpleadosCollectionNewEmpleados != null && !oldCodcategoriaProfesionalOfEmpleadosCollectionNewEmpleados.equals(categoriasProfesionales)) {
                        oldCodcategoriaProfesionalOfEmpleadosCollectionNewEmpleados.getEmpleadosCollection().remove(empleadosCollectionNewEmpleados);
                        oldCodcategoriaProfesionalOfEmpleadosCollectionNewEmpleados = em.merge(oldCodcategoriaProfesionalOfEmpleadosCollectionNewEmpleados);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = categoriasProfesionales.getCodigo();
                if (findCategoriasProfesionales(id) == null) {
                    throw new NonexistentEntityException("The categoriasProfesionales with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriasProfesionales categoriasProfesionales;
            try {
                categoriasProfesionales = em.getReference(CategoriasProfesionales.class, id);
                categoriasProfesionales.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoriasProfesionales with id " + id + " no longer exists.", enfe);
            }
            Collection<Empleados> empleadosCollection = categoriasProfesionales.getEmpleadosCollection();
            for (Empleados empleadosCollectionEmpleados : empleadosCollection) {
                empleadosCollectionEmpleados.setCodcategoriaProfesional(null);
                empleadosCollectionEmpleados = em.merge(empleadosCollectionEmpleados);
            }
            em.remove(categoriasProfesionales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategoriasProfesionales> findCategoriasProfesionalesEntities() {
        return findCategoriasProfesionalesEntities(true, -1, -1);
    }

    public List<CategoriasProfesionales> findCategoriasProfesionalesEntities(int maxResults, int firstResult) {
        return findCategoriasProfesionalesEntities(false, maxResults, firstResult);
    }

    private List<CategoriasProfesionales> findCategoriasProfesionalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoriasProfesionales.class));
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

    public CategoriasProfesionales findCategoriasProfesionales(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoriasProfesionales.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriasProfesionalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoriasProfesionales> rt = cq.from(CategoriasProfesionales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
