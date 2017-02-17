# zipkin-sparkstreaming custom adjuster example

Adjusters are used to clean or change data that goes into zipkin, such that it is more usable. For the sake of example, we assume you have an application adding very large tags in trace data. These slow down the UI and eat up more storage. This is an example adjuster for the spark streaming job, which truncates tags to a configured length.

## Building example
```
mvn clean package
```
## Running adjuster jar with the job

Download spark-streaming jar
```
wget -O zipkin-sparkstreaming-job.jar 'https://search.maven.org/remote_content?g=io.zipkin.sparkstreaming&a=zipkin-sparkstreaming-job&v=LATEST'
```

Run the job by adding adjuster jar to the classpath.

**Note** We can't run the job with -jar flag. [If we use this flag, -cp option is ignored](http://stackoverflow.com/questions/16505992/annotation-scan-not-scanning-external-jars-in-classpath).
```
java -cp "zipkin-sparkstreaming-job.jar:zipkin-sparkstreaming-example-*.jar" \
  zipkin.sparkstreaming.job.ZipkinSparkStreamingJob \
  --zipkin.storage.type=elasticsearch \
  --zipkin.storage.elasticsearch.hosts=http://127.0.0.1:9200 \
  --zipkin.sparkstreaming.stream.kafka.bootstrap-servers=127.0.0.1:9092
```