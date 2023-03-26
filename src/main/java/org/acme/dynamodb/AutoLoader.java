package org.acme.dynamodb;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.StartupEvent;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
@UnlessBuildProfile("prod")
public class AutoLoader {


    @Inject
    DynamoDbClient dynamoDbClient;

    @Inject
    AbstractService abstractService;


    String tableName = "QuarkusFruits";

    public void autoLoad(@Observes StartupEvent ev) {
        createTable();

    }

    public void createTable() {

        try {
            dynamoDbClient.describeTable(builder -> builder.tableName(tableName));
        } catch (Exception e) {
            dynamoDbClient.createTable(CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("PK")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("SK")
                                    .keyType(KeyType.RANGE)
                                    .build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("PK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("SK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("GSI1PK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("GSI1SK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()//,
//                            AttributeDefinition.builder()
//                                    .attributeName("GSI2PK")
//                                    .attributeType(ScalarAttributeType.S)
//                                    .build(),
//                            AttributeDefinition.builder()
//                                    .attributeName("GSI2SK")
//                                    .attributeType(ScalarAttributeType.S)
//                                    .build(),
//                            AttributeDefinition.builder()
//                                    .attributeName("GSI3PK")
//                                    .attributeType(ScalarAttributeType.S)
//                                    .build(),
//                            AttributeDefinition.builder()
//                                    .attributeName("GSI3SK")
//                                    .attributeType(ScalarAttributeType.S)
//                                    .build()

                    )
                    .globalSecondaryIndexes(
                            createGlobalSecondaryIndex("GSI1PK", "GSI1SK")
//                            createGlobalSecondaryIndex("GSI2PK", "GSI2SK"),
//                            createGlobalSecondaryIndex("GSI3PK", "GSI3SK")
//                            GlobalSecondaryIndex.builder()
//                                    .indexName("name-index")
//                                    .keySchema(
//                                            KeySchemaElement.builder()
//                                                    .attributeName("name")
//                                                    .keyType(KeyType.HASH)
//                                                    .build()
//                                    )
//                                    .projection(Projection.builder()
//                                            .projectionType(ProjectionType.ALL)
//                                            .build()
//                                    )
//                                    .provisionedThroughput(ProvisionedThroughput.builder()
//                                            .readCapacityUnits(5L)
//                                            .writeCapacityUnits(5L)
//                                            .build()
//                                    )
//                                    .build()
                    )
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build());

            dynamoDbClient.updateTimeToLive(UpdateTimeToLiveRequest.builder()
                    .tableName(tableName)
                    .timeToLiveSpecification(TimeToLiveSpecification.builder()
                            .enabled(true)
                            .attributeName("TTL")
                            .build())
                    .build());
        }

    }

    private GlobalSecondaryIndex createGlobalSecondaryIndex(String indexPK, String indexSK) {
        return GlobalSecondaryIndex.builder()
                .indexName(indexPK + "_" + indexSK)
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName(indexPK)
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName(indexSK)
                                .keyType(KeyType.RANGE)
                                .build()
                )
                .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(1L)
                        .writeCapacityUnits(1L)
                        .build())
                .build();
    }

}