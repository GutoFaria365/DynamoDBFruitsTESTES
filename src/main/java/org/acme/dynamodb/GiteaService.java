package org.acme.dynamodb;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/api/v1/user")
@RegisterRestClient
@ClientHeaderParam(name = "Authorization", value="token 0673f011ed89245efaa3eb76071654183106bc35")
public interface GiteaService {

    @GET
    String getById();
}

