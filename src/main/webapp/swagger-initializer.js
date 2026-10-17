window.onload = function () {
  // La especificación OpenAPI la sirve OpenApiServlet en /doc (ver web.xml)
  window.ui = SwaggerUIBundle({
    url: "doc/openapi.json",
    dom_id: "#swagger-ui",
    deepLinking: true,
    presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
    plugins: [SwaggerUIBundle.plugins.DownloadUrl],
    layout: "StandaloneLayout"
  });
};
