package org.acme.dynamodb;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@ApplicationScoped
public class FruitSyncService extends AbstractService {

    @Inject
    DynamoDbClient dynamoDB;

    public List<Fruit> findAll() {
        return dynamoDB.scanPaginator(scanRequest()).items().stream()
                .map(Fruit::from)
                .collect(Collectors.toList());
    }

    public List<Fruit> add(Fruit fruit) {
        dynamoDB.putItem(putRequest(fruit));
        return findAll();
    }

    public Fruit get(String name) {
        return Fruit.from(dynamoDB.getItem(getRequest(name)).item());
    }
    public void createFruit() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", AttributeValue.builder().s("USER#1233").build());
        item.put("SK", AttributeValue.builder().s("PROFILE2").build());
        item.put("name", AttributeValue.builder().s("orange").build());
        item.put("description", AttributeValue.builder().s("salty").build());

        dynamoDB.putItem(PutItemRequest.builder()
                .tableName(getTableName())
                .item(item)
                .build());
    }
    public List<Fruit> getByName(String name) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(getTableName())
                .indexName("name-index")
                .keyConditionExpression("name = :n")
                .expressionAttributeValues(Collections.singletonMap(":n", AttributeValue.builder().s(name).build()))
                .build();

        List<Fruit> items = new ArrayList<>();

        QueryResponse response = dynamoDB.query(queryRequest);

        for (Map<String, AttributeValue> item : response.items()) {
            Fruit i = new Fruit();
//            i.setPK(item.get("PK").s());
//            i.setSK(item.get("SK").s());
            i.setName(item.get("name").s());
            i.setDescription(item.get("description").s());
            items.add(i);
        }

        return items;
    }
}