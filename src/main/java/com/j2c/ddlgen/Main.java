package com.j2c.ddlgen;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.schema.TargetType;

import java.util.ArrayList;
import java.util.List;

public class Main {

    @Parameter(
            names = {"--package", "-p"},
            description = "Package containing the classes annotated with @Entity (package javax.persistence). " +
                            "These classes should be available on the runtime classpath.",
            required = true)
    private String basePackage;

    @Parameter(
            names = {"--dialect", "-q"},
            converter = ClassConverter.class,
            validateValueWith = HibernateDialectValidator.class,
            description = "The fully qualified name of the Hibernate dialect to use to generate the DDL. " +
                            "A valid Hibernate dialect extends the abstract class 'org.hibernate.dialect.Dialect'.",
            required = true)
    private Class<? extends Dialect> dialect;

    @Parameter(
            names = {"--targets", "-t"},
            description = "Type of target where the generated DDL will be output. " +
                            "Possible values are 'STDOUT' and 'SCRIPT'. " +
                            "If not specified, defaults to 'STDOUT'.")
    private List<TargetType> targets = new ArrayList<>();

    @Parameter(
            names = {"--directory", "-d"},
            description = "Target directory where the file containing the DDL will be output to. " +
                            "If not specified, defaults to the current directory. " +
                            "Has no effect if 'SCRIPT' is not specified as a target.")
    private String outputDirectory;

    @Parameter(
            names = {"--filename", "-n"},
            description = "Target directory where the file containing the DDL will be output to. " +
                            "If not specified, defaults to 'DDL_<dialect>.sql'. " +
                            "Has no effect if 'SCRIPT' is not specified as a target.")
    private String outputFilename;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        DDLGen.builder()
                .basePackage(main.basePackage)
                .dialect(main.dialect)
                .targets(main.targets)
                .outputDirectory(main.outputDirectory)
                .outputFilename(main.outputFilename)
                .build()
                .generateDDL();
    }

}
