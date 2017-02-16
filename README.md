# zipkin-sparkstreaming custom adjuster example

This example shows how to make a custom adjuster for zipkin-sparkstreaming.
We'll create a jar with a simple adjuster that trims the length of binary annotation values.
This adjuster jar can be used by placing it in the class path of the spark job.

## Steps for creating an adjuster jar

1. Create a java source file that..
  * extends `zipkin.sparkstreaming.Adjuster`
  * has an annotation `org.springframework.context.annotation.Configuration`
2. Create a resource file `META-INF/spring.factories` that contains
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
your.package.FooAdjuster
```
3. Put compiled class and `META-INF/spring.factories` into a jar.

In maven, the following structure would accomplish this.
```
src/main/java/your/package/FooAdjuster.java
src/main/resources/META-INF/spring.factories
```
## Building example
```
mvn clean package
```
## Running the job

Download spark-streaming jar
```
wget -O zipkin-sparkstreaming-job.jar 'https://search.maven.org/remote_content?g=io.zipkin.sparkstreaming&a=zipkin-sparkstreaming-job&v=LATEST'
```

Run the job by adding adjuster jar to the classpath.
Note: We'll can't run the job with -jar flag. [If we use this flag, -cp option is ignored](http://stackoverflow.com/questions/16505992/annotation-scan-not-scanning-external-jars-in-classpath).
```
java -cp "zipkin-sparkstreaming-job.jar:zipkin-sparkstreaming-example-*.jar" \
  zipkin.sparkstreaming.job.ZipkinSparkStreamingJob \
  --zipkin.storage.type=elasticsearch \
  --zipkin.storage.elasticsearch.hosts=http://127.0.0.1:9200 \
  --zipkin.sparkstreaming.stream.kafka.bootstrap-servers=127.0.0.1:9092
```

## Why this should work..
The spring factories thing allows spring boot to pick up and load the class you made. This is called auto-configuration... like java service loader, but better. Read more [here](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html).