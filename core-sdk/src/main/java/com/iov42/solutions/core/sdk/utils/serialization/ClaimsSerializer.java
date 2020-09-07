package com.iov42.solutions.core.sdk.utils.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.iov42.solutions.core.sdk.model.Claims;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.io.IOException;

class ClaimsSerializer extends StdSerializer<Claims> {

    public ClaimsSerializer() {
        this(null);
    }

    public ClaimsSerializer(Class<Claims> t) {
        super(t);
    }

    @Override
    public void serialize(Claims value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartArray();
        for (String plainClaim : value.getPlainClaims()) {
            gen.writeString(PlatformUtils.hashClaim(plainClaim));
        }
        gen.writeEndArray();
    }
}
