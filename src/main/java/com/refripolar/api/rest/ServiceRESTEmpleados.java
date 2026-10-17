package com.refripolar.api.rest;

import com.refripolar.api.model.Empleados;
import com.refripolar.api.dao.EmpleadosJpaController;
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

@Tag(name = "Empleados",
        description = "Datos de los empleados de la empresa")
@Server(url = "/apirefripolar/data")

@Path("empleados")
public class ServiceRESTEmpleados {

    @Operation(
            summary = "Datos de todos los empleados",
            description = "Obtiene los datos de todos los empleados de la empresa"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Empleados.class))
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

        List<Empleados> lista;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            lista = dao.findEmpleadosEntities();
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
            mensaje.put("mensaje", "Error al procesar la petición: " + e.getLocalizedMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @Operation(
            summary = "Datos de un empleado",
            description = "Obtiene los datos de un empleado de la empresa"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Empleados.class))
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
        Empleados empleado;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            empleado = dao.findEmpleados(id);

            if (empleado == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe empleado con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(empleado)
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
            summary = "Mayor id de los empleado",
            description = "Obtiene el id del ultimo empleado registrado"
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
            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            EntityManager em = dao.getEntityManager();
            Query query = em.createQuery("SELECT MAX(emp.id) "
                    + " FROM Empleados emp");
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
            summary = "Datos de los empleados de un departamente",
            description = "Obtiene los datos de todos los empleados de un departamento"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Empleados.class))
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")

    @GET
    @Path("/categoria/{dep}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmpleadosDep(@PathParam("dep") String dep) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        List<Empleados> lista = null;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            EntityManager em = dao.getEntityManager();
            try {
                // Parámetro enlazado: evita inyección JPQL a través de la URL
                lista = em.createQuery("SELECT emp FROM Empleados emp "
                        + "WHERE emp.codcategoriaProfesional.codigo = :codigo", Empleados.class)
                        .setParameter("codigo", dep)
                        .getResultList();
            } finally {
                em.close();
            }
            if ((lista != null) && (!lista.isEmpty())) {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(lista)
                        .build();
            } else {
                statusResul = Response.Status.NO_CONTENT;
                response = Response
                        .status(statusResul)
                        .build();
            }
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
            summary = "Actualiza un empleado",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Empleado actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Empleados empleado) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();

            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            Empleados empleadoFound = dao.findEmpleados(empleado.getId());
            if (empleadoFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe empleado con ID " + empleado.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                empleadoFound.setCodcategoriaProfesional(empleado.getCodcategoriaProfesional());
                empleadoFound.setIdAsignado(empleado.getIdAsignado());
                empleadoFound.setAntiguedad(empleado.getAntiguedad());
                empleadoFound.setImagen(empleado.getImagen());
                empleadoFound.setReconocimientoMedico(empleado.getReconocimientoMedico());
                empleadoFound.setCiudad(empleado.getCiudad());
                empleadoFound.setCp(empleado.getCp());
                empleadoFound.setDireccion(empleado.getDireccion());
                empleadoFound.setMail(empleado.getMail());
                empleadoFound.setTelefono(empleado.getTelefono());
                empleadoFound.setSalario(empleado.getSalario());
                // Grabar los cambios
                dao.edit(empleadoFound);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Empleado con ID " + empleado.getId() + " actualizado");
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
            summary = "Crea un empleado nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Empleado creado")
    @ApiResponse(responseCode = "302",
            description = " Empleado ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petición")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Empleados empleado) {
        EntityManagerFactory emf;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            Empleados empleadoFound = null;
            if ((empleado.getId() != null) && (empleado.getId() != 0)) {
                empleadoFound = dao.findEmpleados(empleado.getId());
            }
            if (empleadoFound != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe el empleado con ID " + empleado.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(empleado);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Empleado " + empleado.getNombre() + " guardado");
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

    @Operation(summary = "Eliminar empleado",
            description = "Eliminar un empleado de la empresa")
    @ApiResponse(responseCode = "200", description = "Empleado eliminado")
    @ApiResponse(responseCode = "400", description = "Empleado no valido")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado")

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

            EmpleadosJpaController dao = new EmpleadosJpaController(emf);
            Empleados empleadoFound = dao.findEmpleados(id);
            if (empleadoFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe el empleado con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Empleado con ID " + id + " eliminado");
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
