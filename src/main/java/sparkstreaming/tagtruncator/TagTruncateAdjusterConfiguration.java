package sparkstreaming.tagtruncator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin.sparkstreaming.Adjuster;

@Configuration
public class TagTruncateAdjusterConfiguration {
  @Bean
  Adjuster tagTruncateAdjuster(@Value("${tagtruncator.max-length:1024}") int maxLength) {
    return new TagTruncateAdjuster(maxLength);
  }
}
