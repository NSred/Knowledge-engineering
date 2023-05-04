package com.owl.api.example.additionalConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalStrings {
    public static String baseIRI;

    @Value("${myapp.global-string}")
    public void setGlobalString(String globalString) {
        baseIRI = globalString;
    }
}
