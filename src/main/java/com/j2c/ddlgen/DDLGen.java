package com.j2c.ddlgen;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.*;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DDLGen {

    @Getter
    private final Set<Class<?>> entities;
    private final EnumSet<TargetType> targets;
    private final File outputFile;
    private final Metadata metadata;

    @Builder
    private DDLGen(@NonNull String basePackage,
                   String outputDirectory,
                   String outputFilename,
                   @NonNull Class<? extends Dialect> dialect,
                   @Singular Set<TargetType> targets) {
        this.targets = toEnumSet(targets);
        entities = getAnnotatedEntityClasses(basePackage);
        metadata = buildMetadata(dialect);
        outputFile = getOutputFile(outputDirectory, outputFilename, dialect.getSimpleName());
    }

    private EnumSet<TargetType> toEnumSet(Set<TargetType> targets) {
        return !targets.isEmpty() ? EnumSet.copyOf(targets) : EnumSet.of(TargetType.STDOUT);
    }

    private Set<Class<?>> getAnnotatedEntityClasses(String basePackage) {
        return new Reflections(basePackage).getTypesAnnotatedWith(Entity.class);
    }

    private Metadata buildMetadata(Class<? extends Dialect> dialect) {
        MetadataSources metadataSources = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", dialect.getName()).build()
        );
        entities.forEach(metadataSources::addAnnotatedClass);
        return metadataSources.buildMetadata();
    }

    private File getOutputFile(String outputDirectory, String outputFilename, String defaultOutputFilename) {
        if (!this.targets.contains(TargetType.SCRIPT)) {
            return null;
        }
        return new File(outputDirectory,
                isNotBlank(outputFilename) ? outputFilename : "DDL_" + defaultOutputFilename + ".sql");
    }

    public void generateDDL() {
        SchemaExport schemaExport = new SchemaExport();
        try {
            if (outputFile != null)
                schemaExport.setOutputFile(outputFile.getCanonicalPath());
            schemaExport.setDelimiter(";");
            schemaExport.setFormat(true);
            schemaExport.execute(targets,
                    SchemaExport.Action.CREATE, metadata);
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

}
