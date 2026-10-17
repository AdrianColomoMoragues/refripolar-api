package com.refripolar.api.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Punto de entrada JAX-RS: todos los recursos REST cuelgan de {@code /data}.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "RefriPolar API",
                version = "1.0.0",
                description = "API REST para la gestión de empleados, clientes y encargos "
                        + "de RefriPolar, una empresa ficticia de refrigeración industrial."
        )
)
@ApplicationPath("data")
public class ApplicationConfig extends Application {
}
