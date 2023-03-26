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
    public List<Fruit> getSingle(@PathParam("name") String name) {
        return service.getByName(name);
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