package com.refripolar.api.rest;

import com.refripolar.api.model.Clientes;
import com.refripolar.api.dao.ClientesJpaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import com.refripolar.api.config.JpaUtil;
import jakarta.persistence.Query;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@Tag(name = "Clientes",
        description = "Datos de los clientes de la empresa")
@Server(url = "/apirefripolar/data")

@Path("clientes")
public class ServiceRESTClientes {

    @Operation(
            summary = "Datos de todos los clientes",
            description = "Obtiene los datos de todos los clientes de la empresa"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Clientes.class))
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        List<Clientes> lista;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            ClientesJpaController dao = new ClientesJpaController(emf);
            lista = dao.findClientesEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                response = Response
                        .status(statusResul)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(lista)
                        .build();
            }
        } catch (Exception e) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @Operation(
            summary = "Datos de un cliente",
            description = "Obtiene los datos de un cliente de la empresa"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Clientes.class))
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") int id) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        Clientes cliente;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            ClientesJpaController dao = new ClientesJpaController(emf);
            cliente = dao.findClientes(id);

            if (cliente == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe cliente con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(cliente)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @Operation(
            summary = "Mayor id de los clientes",
            description = "Obtiene el id del ultimo cliente registrado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito"
    )
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")

    @GET
    @Path("/maxid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMayores() {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        String resultado = "{}";
        try {
            emf = JpaUtil.getEntityManagerFactory();
            ClientesJpaController dao = new ClientesJpaController(emf);
            EntityManager em = dao.getEntityManager();
            Query query = em.createQuery("SELECT MAX(cli.id) "
                    + " FROM Clientes cli");
            Integer max = (Integer) query.getSingleResult();
            em.close();
            int num = (max == null) ? 0 : max;

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(new JSONObject().put("maxid", num));
            resultado = jsonArray.toString();
            statusResul = Response.Status.OK;
            response = Response
                    .status(statusResul)
                    .entity(resultado)
                    .build();
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición: " + ex.getLocalizedMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @Operation(
            summary = "Actualiza un cliente",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Cliente actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Clientes cliente) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();

            ClientesJpaController dao = new ClientesJpaController(emf);
            Clientes clienteFound = dao.findClientes(cliente.getId());
            if (clienteFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe cliente con ID " + cliente.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del libro encontrado
                clienteFound.setTelefono(cliente.getTelefono());
                clienteFound.setCorreo(cliente.getCorreo());
                clienteFound.setLocalidad(cliente.getLocalidad());
                clienteFound.setDireccion(cliente.getDireccion());
                clienteFound.setCaracteristicas(cliente.getCaracteristicas());
                // Grabar los cambios
                dao.edit(clienteFound);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Cliente con ID " + cliente.getId() + " actualizado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @Operation(
            summary = "Crea un cliente nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Cliente creado")
    @ApiResponse(responseCode = "302",
            description = " Cliente ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Clientes cliente) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            ClientesJpaController dao = new ClientesJpaController(emf);
            Clientes clienteFound = null;
            if ((cliente.getId() != null) && (cliente.getId() != 0)) {
                clienteFound = dao.findClientes(cliente.getId());
            }
            if (clienteFound != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe el cliente con ID " + cliente.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(cliente);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Cliente " + cliente.getNombre() + " guardado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @Operation(summary = "Eliminar cliente",
            description = "Eliminar un cliente de la empresa")
    @ApiResponse(responseCode = "200", description = "Cliente eliminado")
    @ApiResponse(responseCode = "400", description = "Cliente no valido")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();

            ClientesJpaController dao = new ClientesJpaController(emf);
            Clientes clienteFound = dao.findClientes(id);
            if (clienteFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe el cliente con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Cliente con ID " + id + " eliminado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }
}
