package org.acme.dynamodb;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;


@Path("/fruits")
public class FruitResource {

    @Inject
    FruitSyncService service;

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