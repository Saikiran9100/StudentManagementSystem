package com.example.MiniProject.converter;

import com.example.MiniProject.Utils.AESUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Converter
@Component

public class AccountNumberEncryptConverter implements AttributeConverter<String, String> {

    private final AESUtil aesUtil;

    public AccountNumberEncryptConverter(AESUtil aesUtil) {
        this.aesUtil = aesUtil;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {

        if (attribute == null) {
            return null;
        }

        return aesUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {

        if (dbData == null) {
            return null;
        }

        return aesUtil.decrypt(dbData);
    }
}