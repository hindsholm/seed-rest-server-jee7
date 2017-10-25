package io.openapitools.rest.common.rs.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import io.openapitools.rest.common.rs.filter.OriginFilter;

/**
 * Responds with OpenAPI documentation JSON file created by Swagger Plugin
 * Used with the rest-service-archetype
 */
public class ApiServlet extends HttpServlet {

    private static final String SWAGGER_LOCATION = "/WEB-INF/api/swagger.json";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MediaType.APPLICATION_JSON);
        try {
            Path swDoc = Paths.get(getServletContext().getResource(SWAGGER_LOCATION).toURI());
            Files.copy(swDoc, resp.getOutputStream());
        } catch (URISyntaxException e) {
            getServletContext().log("Could not convert Swagger location to URL", e);
        }
        for (Map.Entry<String, String> header : OriginFilter.HEADERS.entrySet()) {
            resp.setHeader(header.getKey(), header.getValue());
        }
    }
}
