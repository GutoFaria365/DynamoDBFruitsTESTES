package org.acme.dynamodb;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;


@Path("/fruits")
public class FruitResource {

    @Inject
    FruitSyncService service;

    @RestClient
    GiteaService giteaService;

    @GET
    @Path("/user")
    public String getUser(){
        return giteaService.getById("token 0673f011ed89245efaa3eb76071654183106bc35");
    }
    @GET
    public List<Fruit> getAll() {
        return service.findAll();
    }

    @GET
    @Path("{name}")
    public List<Fruit> getByName(@PathParam("name") String name) {
        return service.getByName(name);
    }
    @GET
    @Path("taste/{taste}")
    public List<Fruit> getByTaste(@PathParam("taste") String taste) {
        return service.getByTaste(taste);
    }
    @GET
    @Path("single/{name-taste}")
    public List<Fruit> getSingle(@PathParam("name-taste") String nametaste) {
        return service.getSingle(nametaste);
    }

    @POST
    public List<Fruit> add(Fruit fruit) {
        service.add(fruit);
        return getAll();
    }
//    @POST
//    public List<Fruit> createFruit() {
//        service.createFruit();
//        return getAll();
//    }
}