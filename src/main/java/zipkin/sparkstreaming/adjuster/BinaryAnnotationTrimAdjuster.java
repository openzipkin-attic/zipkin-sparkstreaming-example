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
package zipkin.sparkstreaming.adjuster;

import zipkin.BinaryAnnotation;
import zipkin.Span;
import zipkin.sparkstreaming.Adjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This adjuster will trim the value of binary annotations of type BYTES and STRING
 */
public class BinaryAnnotationTrimAdjuster extends Adjuster {

  public static final int VALUE_MAX_LENGTH = 1024;

  @Override
  protected boolean shouldAdjust(Span span) {
    return span.binaryAnnotations.stream()
        .filter(ba -> ((ba.type.equals(BinaryAnnotation.Type.BYTES)
                           || ba.type.equals(BinaryAnnotation.Type.STRING))
                           && ba.value.length > VALUE_MAX_LENGTH)).findFirst().isPresent();
  }

  @Override
  protected Span adjust(Span span) {
    List<BinaryAnnotation> trimmedBinaryAnnotations = new ArrayList<>();
    span.binaryAnnotations.forEach(ba -> trimmedBinaryAnnotations.add(trimValue(ba)));
    return span.toBuilder().binaryAnnotations(trimmedBinaryAnnotations).build();
  }

  private BinaryAnnotation trimValue(BinaryAnnotation ba) {
    return ba.toBuilder()
        .value(ba.value.length > VALUE_MAX_LENGTH ?
               Arrays.copyOfRange(ba.value, 0, VALUE_MAX_LENGTH) : ba.value)
        .build();
  }
}
