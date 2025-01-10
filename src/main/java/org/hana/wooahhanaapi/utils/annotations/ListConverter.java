package org.hana.wooahhanaapi.utils.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Converter
public class ListConverter implements AttributeConverter<List<?>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<?> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 실패: " + attribute, e);
        }
    }

    @Override
    public List<?> convertToEntityAttribute(String dbData) {
        try {
            List<Object> tempList = objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class));

            if (tempList.stream().allMatch(item -> isUuid(item.toString()))) {
                return tempList.stream().map(item -> UUID.fromString(item.toString())).collect(Collectors.toList());
            }
            return tempList.stream().map(Object::toString).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패: " + dbData, e);
        }
    }

    private boolean isUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
