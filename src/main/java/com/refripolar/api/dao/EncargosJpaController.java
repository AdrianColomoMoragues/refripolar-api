package com.refripolar.api.dao;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.refripolar.api.model.Clientes;
import com.refripolar.api.model.Empleados;
import com.refripolar.api.model.Encargos;
import com.refripolar.api.dao.exceptions.NonexistentEntityException;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class EncargosJpaController implements Serializable {

    public EncargosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encargos encargos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes idCliente = encargos.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                encargos.setIdCliente(idCliente);
            }
            Empleados idEncargado = encargos.getIdEncargado();
            if (idEncargado != null) {
                idEncargado = em.getReference(idEncargado.getClass(), idEncargado.getId());
                encargos.setIdEncargado(idEncargado);
            }
            em.persist(encargos);
            if (idCliente != null) {
                idCliente.getEncargosCollection().add(encargos);
                idCliente = em.merge(idCliente);
            }
            if (idEncargado != null) {
                idEncargado.getEncargosCollection().add(encargos);
                idEncargado = em.merge(idEncargado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encargos encargos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encargos persistentEncargos = em.find(Encargos.class, encargos.getId());
            Clientes idClienteOld = persistentEncargos.getIdCliente();
            Clientes idClienteNew = encargos.getIdCliente();
            Empleados idEncargadoOld = persistentEncargos.getIdEncargado();
            Empleados idEncargadoNew = encargos.getIdEncargado();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                encargos.setIdCliente(idClienteNew);
            }
            if (idEncargadoNew != null) {
                idEncargadoNew = em.getReference(idEncargadoNew.getClass(), idEncargadoNew.getId());
                encargos.setIdEncargado(idEncargadoNew);
            }
            encargos = em.merge(encargos);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getEncargosCollection().remove(encargos);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getEncargosCollection().add(encargos);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idEncargadoOld != null && !idEncargadoOld.equals(idEncargadoNew)) {
                idEncargadoOld.getEncargosCollection().remove(encargos);
                idEncargadoOld = em.merge(idEncargadoOld);
            }
            if (idEncargadoNew != null && !idEncargadoNew.equals(idEncargadoOld)) {
                idEncargadoNew.getEncargosCollection().add(encargos);
                idEncargadoNew = em.merge(idEncargadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encargos.getId();
                if (findEncargos(id) == null) {
                    throw new NonexistentEntityException("The encargos with id " + id + " no longer exists.");
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
            Encargos encargos;
            try {
                encargos = em.getReference(Encargos.class, id);
                encargos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encargos with id " + id + " no longer exists.", enfe);
            }
            Clientes idCliente = encargos.getIdCliente();
            if (idCliente != null) {
                idCliente.getEncargosCollection().remove(encargos);
                idCliente = em.merge(idCliente);
            }
            Empleados idEncargado = encargos.getIdEncargado();
            if (idEncargado != null) {
                idEncargado.getEncargosCollection().remove(encargos);
                idEncargado = em.merge(idEncargado);
            }
            em.remove(encargos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encargos> findEncargosEntities() {
        return findEncargosEntities(true, -1, -1);
    }

    public List<Encargos> findEncargosEntities(int maxResults, int firstResult) {
        return findEncargosEntities(false, maxResults, firstResult);
    }

    private List<Encargos> findEncargosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encargos.class));
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

    public Encargos findEncargos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encargos.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncargosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encargos> rt = cq.from(Encargos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
