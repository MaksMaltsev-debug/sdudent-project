package com.maltsev.clas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maltsev.jwt.UserAccount;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class TestUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new JavaTimeModule());
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }


    public static UserAccount buildDefaultAccount() {
        return UserAccount.builder()
                .userId("1")
                .build();
    }
}
