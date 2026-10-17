package com.refripolar.api.dao;

import com.refripolar.api.model.Clientes;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.refripolar.api.model.Encargos;
import com.refripolar.api.dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) {
        if (clientes.getEncargosCollection() == null) {
            clientes.setEncargosCollection(new ArrayList<Encargos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Encargos> attachedEncargosCollection = new ArrayList<Encargos>();
            for (Encargos encargosCollectionEncargosToAttach : clientes.getEncargosCollection()) {
                encargosCollectionEncargosToAttach = em.getReference(encargosCollectionEncargosToAttach.getClass(), encargosCollectionEncargosToAttach.getId());
                attachedEncargosCollection.add(encargosCollectionEncargosToAttach);
            }
            clientes.setEncargosCollection(attachedEncargosCollection);
            em.persist(clientes);
            for (Encargos encargosCollectionEncargos : clientes.getEncargosCollection()) {
                Clientes oldIdClienteOfEncargosCollectionEncargos = encargosCollectionEncargos.getIdCliente();
                encargosCollectionEncargos.setIdCliente(clientes);
                encargosCollectionEncargos = em.merge(encargosCollectionEncargos);
                if (oldIdClienteOfEncargosCollectionEncargos != null) {
                    oldIdClienteOfEncargosCollectionEncargos.getEncargosCollection().remove(encargosCollectionEncargos);
                    oldIdClienteOfEncargosCollectionEncargos = em.merge(oldIdClienteOfEncargosCollectionEncargos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getId());
            Collection<Encargos> encargosCollectionOld = persistentClientes.getEncargosCollection();
            Collection<Encargos> encargosCollectionNew = clientes.getEncargosCollection();
            Collection<Encargos> attachedEncargosCollectionNew = new ArrayList<Encargos>();
            for (Encargos encargosCollectionNewEncargosToAttach : encargosCollectionNew) {
                encargosCollectionNewEncargosToAttach = em.getReference(encargosCollectionNewEncargosToAttach.getClass(), encargosCollectionNewEncargosToAttach.getId());
                attachedEncargosCollectionNew.add(encargosCollectionNewEncargosToAttach);
            }
            encargosCollectionNew = attachedEncargosCollectionNew;
            clientes.setEncargosCollection(encargosCollectionNew);
            clientes = em.merge(clientes);
            for (Encargos encargosCollectionOldEncargos : encargosCollectionOld) {
                if (!encargosCollectionNew.contains(encargosCollectionOldEncargos)) {
                    encargosCollectionOldEncargos.setIdCliente(null);
                    encargosCollectionOldEncargos = em.merge(encargosCollectionOldEncargos);
                }
            }
            for (Encargos encargosCollectionNewEncargos : encargosCollectionNew) {
                if (!encargosCollectionOld.contains(encargosCollectionNewEncargos)) {
                    Clientes oldIdClienteOfEncargosCollectionNewEncargos = encargosCollectionNewEncargos.getIdCliente();
                    encargosCollectionNewEncargos.setIdCliente(clientes);
                    encargosCollectionNewEncargos = em.merge(encargosCollectionNewEncargos);
                    if (oldIdClienteOfEncargosCollectionNewEncargos != null && !oldIdClienteOfEncargosCollectionNewEncargos.equals(clientes)) {
                        oldIdClienteOfEncargosCollectionNewEncargos.getEncargosCollection().remove(encargosCollectionNewEncargos);
                        oldIdClienteOfEncargosCollectionNewEncargos = em.merge(oldIdClienteOfEncargosCollectionNewEncargos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientes.getId();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            Collection<Encargos> encargosCollection = clientes.getEncargosCollection();
            for (Encargos encargosCollectionEncargos : encargosCollection) {
                encargosCollectionEncargos.setIdCliente(null);
                encargosCollectionEncargos = em.merge(encargosCollectionEncargos);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
