package com.j2c.ddlgen;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import org.hibernate.dialect.Dialect;

public class HibernateDialectValidator implements IValueValidator<Class<?>> {

    @Override
    public void validate(String name, Class<?> dialect) throws ParameterException {
        if (!Dialect.class.isAssignableFrom(dialect)) {
            throw new ParameterException("Dialect " + name + " must extend 'org.hibernate.dialect.Dialect'.");
        }
    }

}
