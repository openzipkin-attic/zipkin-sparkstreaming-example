package sparkstreaming.tagtruncator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zipkin.sparkstreaming.Adjuster;

import static org.springframework.boot.test.util.EnvironmentTestUtils.addEnvironment;


public class TagTruncateAdjusterConfigurationTest {

  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

  @Test
  public void providesAdjuster() {
    context.register(TagTruncateAdjusterConfiguration.class);
    context.refresh();

    Adjuster adjuster = context.getBean(Adjuster.class);
    assertThat(adjuster).isInstanceOf(TagTruncateAdjuster.class);
    assertThat(((TagTruncateAdjuster)adjuster).maxLength == 1024);
  }

  @Test
  public void overrideMaxLengthProperty() {
    int maxLength = 255;
    addEnvironment(context, "tagtruncator.max-length:" + maxLength);
    context.register(TagTruncateAdjusterConfiguration.class);
    context.refresh();

    Adjuster adjuster = context.getBean(Adjuster.class);
    assertThat(((TagTruncateAdjuster)adjuster).maxLength == maxLength);
  }
}
