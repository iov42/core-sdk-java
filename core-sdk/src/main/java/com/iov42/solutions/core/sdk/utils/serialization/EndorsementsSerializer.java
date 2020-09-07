package com.iov42.solutions.core.sdk.utils.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.iov42.solutions.core.sdk.model.Endorsements;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.io.IOException;

class EndorsementsSerializer extends StdSerializer<Endorsements> {

    public EndorsementsSerializer() {
        this(null);
    }

    public EndorsementsSerializer(Class<Endorsements> t) {
        super(t);
    }

    @Override
    public void serialize(Endorsements value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        String signingPrefix = value.getSubjectId() + ";";
        if (value.getSubjectTypeId() != null) {
            signingPrefix += value.getSubjectTypeId() + ";";
        }
        for (String plainClaim : value.getClaims().getPlainClaims()) {
            String hash = PlatformUtils.hashClaim(plainClaim);
            gen.writeStringField(hash, PlatformUtils.sign(value.getEndorserInfo(), signingPrefix + hash).getSignature());
        }
        gen.writeEndObject();
    }
}
