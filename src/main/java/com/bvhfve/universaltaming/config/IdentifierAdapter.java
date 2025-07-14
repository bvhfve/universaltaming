package com.bvhfve.universaltaming.config;

import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class IdentifierAdapter implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {

    @Override
    public JsonElement serialize(Identifier identifier, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(identifier.toString());
    }

    @Override
    public Identifier deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Identifier.tryParse(jsonElement.getAsString());
        } catch (Exception e) {
            throw new JsonParseException("Invalid identifier: " + jsonElement.getAsString(), e);
        }
    }
}