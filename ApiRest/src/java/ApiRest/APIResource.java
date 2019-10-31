/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApiRest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author nazaret
 */
@Path("API")
public class APIResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of APIResource
     */
    public APIResource() {
    }

    /**
     * Retrieves representation of an instance of ApiRest.APIResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("image/jpeg")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of APIResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes("image/jpeg")
    public void putXml(String content) {
    }
    
    @POST
    @Consumes("image/jpeg")
    public void postXml(String content) {
    }
}
