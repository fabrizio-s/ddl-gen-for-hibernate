1) Run from the same directory that contains the pom.xml for your jar module containing your JPA annotated @Entity classes: 

   mvn clean dependency:build-classpath package -Dmdep.outputFile=target/classpath.txt -Dmaven.test.skip=true

This outputs a 'classpath.txt' that contains the module's dependencies as a string,
and also builds the jar (called 'j2c-domain-0.0.1-SNAPSHOT.jar' in the next step). 

2) Run:

   java -cp $(cat classpath.txt):j2c-domain-0.0.1-SNAPSHOT.jar:ddl-gen-1.0.0.jar com.j2c.ddlgen.Main --package "com.j2c.j2c.domain.entity" --dialect org.hibernate.dialect.PostgreSQL10Dialect --targets SCRIPT --filename schema.sql

where --package specifies the package that contains your JPA entities. Outputs 'schema.sql' to run against the database.
