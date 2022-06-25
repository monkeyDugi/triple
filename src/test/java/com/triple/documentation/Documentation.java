package com.triple.documentation;

import com.triple.util.DataBaseCleanUp;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Documentation {
    @LocalServerPort
    private int port;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    private RequestSpecification spec;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        dataBaseCleanUp.execute();

        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    protected RequestSpecification givenRestDocsRequestFields(String identifier, FieldDescriptor[] fieldDescriptors) {
        return RestAssured.given(this.spec)
                .filter(document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(fieldDescriptors)
                        )
                );
    }

    protected RequestSpecification givenRestDocsRequestAndResponseFields(String identifier, FieldDescriptor[] requestFieldDescriptors,
                                                                         FieldDescriptor[] responseFieldDescriptors) {
        return RestAssured.given(this.spec).log().all()
                .filter(document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(requestFieldDescriptors),
                        relaxedResponseFields(responseFieldDescriptors)
                        )
                );
    }
}
