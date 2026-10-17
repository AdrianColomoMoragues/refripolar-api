package com.refripolar.api.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Crea una única {@link EntityManagerFactory} para toda la aplicación
 * (crearla es costoso, así que no se hace en cada petición) y la cierra
 * cuando Tomcat detiene la aplicación.
 *
 * <p>La conexión se configura con variables de entorno, de modo que no hay
 * credenciales en el código:
 * <ul>
 *   <li>{@code DB_URL}  — p. ej. {@code jdbc:mysql://localhost:3306/bdrefripolarv1}</li>
 *   <li>{@code DB_USER}</li>
 *   <li>{@code DB_PASSWORD}</li>
 * </ul>
 */
@WebListener
public final class JpaUtil implements ServletContextListener {

    public static final String PERSISTENCE_UNIT = "ApiRefriPolarPU";

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/bdrefripolarv1"
            + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    private static volatile EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (JpaUtil.class) {
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, connectionProperties());
                }
            }
        }
        return emf;
    }

    static Map<String, String> connectionProperties() {
        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", env("DB_URL", DEFAULT_URL));
        props.put("jakarta.persistence.jdbc.user", env("DB_USER", "refripolar"));
        props.put("jakarta.persistence.jdbc.password", env("DB_PASSWORD", ""));
        return props;
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
