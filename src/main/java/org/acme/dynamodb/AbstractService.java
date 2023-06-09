package org.acme.dynamodb;

import java.util.*;

import software.amazon.awssdk.services.dynamodb.model.*;

public abstract class AbstractService {

    public final static String FRUIT_NAME_COL = "fruitName";
    public final static String FRUIT_DESC_COL = "fruitDescription";

    public String getTableName() {
        return "QuarkusFruits";
    }

    protected ScanRequest scanRequest() {
        return ScanRequest.builder().tableName(getTableName())
                .attributesToGet(FRUIT_NAME_COL, FRUIT_DESC_COL).build();
    }

    protected PutItemRequest putRequest(Fruit fruit) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", AttributeValue.builder().s("FRUIT#").build());
        item.put("SK", AttributeValue.builder().s("FRUIT#" + fruit.getName().toUpperCase() + "#TASTE#" + fruit.getDescription().toUpperCase()).build());
        item.put(FRUIT_NAME_COL, AttributeValue.builder().s(fruit.getName()).build());
        item.put(FRUIT_DESC_COL, AttributeValue.builder().s(fruit.getDescription()).build());
        item.put("GSI1PK", AttributeValue.builder().s("NAME#").build());
        item.put("GSI1SK", AttributeValue.builder().s("NAME#" + fruit.getName().toUpperCase()).build());
        item.put("GSI2PK", AttributeValue.builder().s("TASTE#").build());
        item.put("GSI2SK", AttributeValue.builder().s("TASTE#" + fruit.getDescription().toUpperCase()).build());

        return PutItemRequest.builder()
                .tableName(getTableName())
                .item(item)
                .build();
    }

    protected GetItemRequest getRequest(String name) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(FRUIT_NAME_COL, AttributeValue.builder().s(name).build());

        return GetItemRequest.builder()
                .tableName(getTableName())
                .key(key)
                .attributesToGet(FRUIT_NAME_COL, FRUIT_DESC_COL)
                .build();
    }
}