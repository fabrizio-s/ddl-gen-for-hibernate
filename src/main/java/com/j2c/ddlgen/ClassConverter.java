package com.j2c.ddlgen;

import com.beust.jcommander.IStringConverter;

public class ClassConverter implements IStringConverter<Class<?>> {

    @Override
    public Class<?> convert(String dialect) {
        try {
            return Class.forName(dialect);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

}
