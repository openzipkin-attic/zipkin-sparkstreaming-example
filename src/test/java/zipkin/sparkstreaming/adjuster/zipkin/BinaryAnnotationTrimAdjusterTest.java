/**
 * Copyright 2017 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package zipkin.sparkstreaming.adjuster.zipkin;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import zipkin.BinaryAnnotation;
import zipkin.Endpoint;
import zipkin.Span;
import zipkin.sparkstreaming.Adjuster;
import zipkin.sparkstreaming.adjuster.BinaryAnnotationTrimAdjuster;

import java.util.Arrays;

public class BinaryAnnotationTrimAdjusterTest {

  @Test
  public void TestTrimming() {
    Adjuster adjuster = new BinaryAnnotationTrimAdjuster();
    Span originalSpan = span(BinaryAnnotationTrimAdjuster.VALUE_MAX_LENGTH + 1);
    Span expectedSpan = span(BinaryAnnotationTrimAdjuster.VALUE_MAX_LENGTH);

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
