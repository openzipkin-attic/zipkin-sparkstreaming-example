package sparkstreaming.tagtruncator;

import zipkin.BinaryAnnotation;
import zipkin.Span;
import zipkin.sparkstreaming.Adjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This adjuster will truncate the value of tags (binary annotations of type STRING) to maxLength
 */
public class TagTruncateAdjuster extends Adjuster {

  final int maxLength;

  public TagTruncateAdjuster(int maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  protected boolean shouldAdjust(Span span) {
    return span.binaryAnnotations.stream()
        .filter(ba -> (shouldTruncate(ba))).findFirst().isPresent();
  }

  private boolean shouldTruncate(BinaryAnnotation binaryAnnotation) {
    return binaryAnnotation.type.equals(BinaryAnnotation.Type.STRING)
        && binaryAnnotation.value.length > maxLength;
  }

  @Override
  protected Span adjust(Span span) {
    List<BinaryAnnotation> truncatedAnnotations = new ArrayList<>();
    span.binaryAnnotations.forEach(ba -> truncatedAnnotations.add(truncateValue(ba)));
    return span.toBuilder().binaryAnnotations(truncatedAnnotations).build();
  }

  private BinaryAnnotation truncateValue(BinaryAnnotation binaryAnnotation) {
    if (shouldTruncate(binaryAnnotation)) {
      return binaryAnnotation.toBuilder()
          .value(Arrays.copyOfRange(binaryAnnotation.value, 0, maxLength))
          .build();
    } else {
      return binaryAnnotation;
    }
  }
}
