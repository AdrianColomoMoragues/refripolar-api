package com.refripolar.api.rest;

import com.refripolar.api.model.CategoriasProfesionales;
import com.refripolar.api.dao.CategoriasProfesionalesJpaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManagerFactory;
import com.refripolar.api.config.JpaUtil;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Tag(name = "Categorias profesionales",
        description = "Datos de las categorias profesionales de los empleados")
@Server(url = "/apirefripolar/data")

@Path("categoriasprofesionales")
public class ServiceRESTCategoriasProfesionales {

    @Operation(
            summary = "Datos de todos las categorias profesionales",
            description = "Obtiene los datos de las categorias profesionales de los empleados"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acción realizada con éxito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = CategoriasProfesionales.class))
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

        List<CategoriasProfesionales> lista;
        try {
            emf = JpaUtil.getEntityManagerFactory();
            CategoriasProfesionalesJpaController dao = new CategoriasProfesionalesJpaController(emf);
            lista = dao.findCategoriasProfesionalesEntities();
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
}
