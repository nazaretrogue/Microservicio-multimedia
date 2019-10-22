/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApiRest;

import java.util.Base64;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author nazaret
 */
@Path("{id}")
public class PatronesApi {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PatronesApi
     */
    public PatronesApi() {
    }

    /**
     * Retrieves representation of an instance of ApiRest.PatronesApi
     * @param id resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("image/jpeg")
    public String getImage(@PathParam("id") String id) {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of PatronesApi
     * @param id resource URI parameter
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("image/jpeg")
    public void putImage(@PathParam("id") String id, String content) {
        
        //String encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
    }
}