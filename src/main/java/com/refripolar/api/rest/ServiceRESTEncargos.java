package com.refripolar.api.rest;

import com.refripolar.api.model.Empleados;
import com.refripolar.api.model.Encargos;
import com.refripolar.api.dao.EncargosJpaController;
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

@Tag(name = "Encargos",
        description = "Datos de los encargos de la empresa")
@Server(url = "/apirefripolar/data")

@Path("encargos")
public class ServiceRESTEncargos {

    @Operation(
            summary = "Datos de todos los encargos",
            description = "Obtiene los datos de todos los encargos de la empresa"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Encargos.class))
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

        List<Encargos> lista;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EncargosJpaController dao = new EncargosJpaController(emf);
            lista = dao.findEncargosEntities();
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
            summary = "Datos de un encargo",
            description = "Obtiene los datos de un encargo de la empresa"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Encargos.class))
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
        Encargos encargo;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EncargosJpaController dao = new EncargosJpaController(emf);
            encargo = dao.findEncargos(id);

            if (encargo == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe encargo con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(encargo)
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
            summary = "Mayor id de los encargos",
            description = "Obtiene el id del ultimo encargos registrado"
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
            EncargosJpaController dao = new EncargosJpaController(emf);
            EntityManager em = dao.getEntityManager();
            Query query = em.createQuery("SELECT MAX(enc.id) "
                    + " FROM Encargos enc");
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
            summary = "Actualiza un encargo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Encargo actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Encargos encargo) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();

            EncargosJpaController dao = new EncargosJpaController(emf);
            Encargos encargoFound = dao.findEncargos(encargo.getId());
            if (encargoFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe encargo con ID " + encargo.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del libro encontrado
                encargoFound.setNombre(encargo.getNombre());
                encargoFound.setIdEncargado(encargo.getIdEncargado());
                encargoFound.setIdCliente(encargo.getIdCliente());
                encargoFound.setTipo(encargo.getTipo());
                encargoFound.setPrioridad(encargo.getPrioridad());
                encargoFound.setPorcentaje(encargo.getPorcentaje());
                if (encargo.getPorcentaje() == 100) {
                    encargoFound.setTerminada(true);
                } else {
                    encargoFound.setTerminada(false);
                }
                ServiceRESTEmpleados serviceEmpleados = new ServiceRESTEmpleados();
                for (Empleados empleados : encargoFound.getEmpleadosCollection()) {
                    empleados.setIdAsignado(null);
                    serviceEmpleados.put(empleados);
                }
                for (Empleados empleados : encargo.getEmpleadosCollection()) {
                    empleados.setIdAsignado(encargo);
                    serviceEmpleados.put(empleados);
                }
                // Grabar los cambios
                dao.edit(encargoFound);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Encargo con ID " + encargo.getId() + " actualizado");
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
            summary = "Crea un encargo nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Encargo creado")
    @ApiResponse(responseCode = "302",
            description = " Encargo ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")
    @POST

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Encargos encargo) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EncargosJpaController dao = new EncargosJpaController(emf);
            Encargos encargoFound = null;
            if ((encargo.getId() != null) && (encargo.getId() != 0)) {
                encargoFound = dao.findEncargos(encargo.getId());
            }
            if (encargoFound != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe el encargo con ID " + encargo.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                ServiceRESTEmpleados serviceEmpleados = new ServiceRESTEmpleados();
                dao.create(encargo);
                for (Empleados empleados : encargo.getEmpleadosCollection()) {
                    empleados.setIdAsignado(encargo);
                    serviceEmpleados.put(empleados);
                }
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Encargo " + encargo.getNombre() + " guardado");
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

    @Operation(summary = "Eliminar encargo",
            description = "Eliminar un encargo de la empresa")
    @ApiResponse(responseCode = "200", description = "Encargo eliminado")
    @ApiResponse(responseCode = "400", description = "Encargo no valido")
    @ApiResponse(responseCode = "404", description = "Encargo no encontrado")

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();

            EncargosJpaController dao = new EncargosJpaController(emf);
            Encargos encargoFound = dao.findEncargos(id);
            if (encargoFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe el cliente con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                ServiceRESTEmpleados serviceEmpleados = new ServiceRESTEmpleados();
                for (Empleados empleados : encargoFound.getEmpleadosCollection()) {
                    empleados.setIdAsignado(null);
                    serviceEmpleados.put(empleados);
                }
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
