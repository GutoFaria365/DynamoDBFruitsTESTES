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
    public List<Fruit> getSingle(String nametaste) {
        String[] parts = nametaste.split("-");
        String name = parts[0];
        String taste = parts[1];
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", AttributeValue.builder().s("FRUIT#").build());
        expressionAttributeValues.put(":sk", AttributeValue.builder().s("FRUIT#".concat(name.toUpperCase().concat("#TASTE#".concat(taste.toUpperCase())))).build());
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(getTableName())
                .keyConditionExpression("PK= :pk and SK= :sk")
                .expressionAttributeValues(expressionAttributeValues)
                .build();


        List<Fruit> items = new ArrayList<>();

        QueryResponse response = dynamoDB.query(queryRequest);

        for (Map<String, AttributeValue> item : response.items()) {
            Fruit i = new Fruit();
            i.setName(item.get("fruitName").s());
            i.setDescription(item.get("fruitDescription").s());
            items.add(i);
        }

        return items;
    }
    public List<Fruit> getByName(String name) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":gsi1pk", AttributeValue.builder().s("NAME#").build());
        expressionAttributeValues.put(":gsi1sk", AttributeValue.builder().s("NAME#".concat(name.toUpperCase())).build());
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(getTableName())
                .indexName("GSI1PK_GSI1SK")
                .keyConditionExpression("GSI1PK= :gsi1pk and GSI1SK= :gsi1sk")
                .expressionAttributeValues(expressionAttributeValues)
                .build();


        List<Fruit> items = new ArrayList<>();

        QueryResponse response = dynamoDB.query(queryRequest);

        for (Map<String, AttributeValue> item : response.items()) {
            Fruit i = new Fruit();
            i.setName(item.get("fruitName").s());
            i.setDescription(item.get("fruitDescription").s());
            items.add(i);
        }

        return items;
    }

    public List<Fruit> getByTaste(String taste) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":gsi2pk", AttributeValue.builder().s("TASTE#").build());
        expressionAttributeValues.put(":gsi2sk", AttributeValue.builder().s("TASTE#".concat(taste.toUpperCase())).build());
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(getTableName())
                .indexName("GSI2PK_GSI2SK")
                .keyConditionExpression("GSI2PK= :gsi2pk and GSI2SK= :gsi2sk")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        List<Fruit> items = new ArrayList<>();

        QueryResponse response = dynamoDB.query(queryRequest);

        for (Map<String, AttributeValue> item : response.items()) {
            Fruit i = new Fruit();
            i.setName(item.get("fruitName").s());
            i.setDescription(item.get("fruitDescription").s());
            items.add(i);
        }

        return items;
    }
}