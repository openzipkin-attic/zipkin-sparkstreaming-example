package sparkstreaming.tagtruncator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import zipkin.BinaryAnnotation;
import zipkin.Endpoint;
import zipkin.Span;
import zipkin.sparkstreaming.Adjuster;

import java.util.Arrays;

public class TagTruncateAdjusterTest {

  @Test
  public void TestTruncation() {
    int maxLength = 10;
    Adjuster adjuster = new TagTruncateAdjuster(maxLength);
    Span originalSpan = span(maxLength + 1);
    Span expectedSpan = span(maxLength);

    Iterable<Span> adjusted = adjuster.adjust(Arrays.asList(originalSpan));
    assertThat(adjusted.iterator().next()).isEqualByComparingTo(expectedSpan);
  }

  private Span span(int binaryAnnotationValueSize) {
    Endpoint e = Endpoint.builder().serviceName("service").ipv4(127 << 24 | 1).port(8080).build();
    return Span.builder().traceId(1L).id(1L)
        .timestamp(1000L)
        .duration(200L)
        .name("name")
        .addBinaryAnnotation(
            BinaryAnnotation.create("lc", new String(new byte[binaryAnnotationValueSize]), e))
        .build();
  }

}
