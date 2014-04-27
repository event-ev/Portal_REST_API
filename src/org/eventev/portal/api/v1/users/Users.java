package org.eventev.portal.api.v1.users;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/users")
public class Users {
    @Context
    private UriInfo context;

    /**
     * Default constructor. 
     */
    public Users() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieves representation of an instance of Users
     * @return an instance of String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        // TODO return proper representation object
        throw new UnsupportedOperationException("Möp");
    	//return new Todo();
    }

    /**
     * PUT method for updating or creating an instance of Users
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

}