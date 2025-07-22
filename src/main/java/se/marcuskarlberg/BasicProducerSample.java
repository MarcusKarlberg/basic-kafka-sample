package se.marcuskarlberg;

import static java.lang.System.*;

import java.util.*;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.*;

public final class BasicProducerSample {
  BasicProducerSample() {}

  public void runProducer() throws InterruptedException {
    final var topic = "getting-started";

    final Map<String, Object> config =
      Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

    try (var producer = new KafkaProducer<String, String>(config)) {
      while (true) {
        final var key = "myKey";
        final var value = new Date().toString();
        out.format("Publishing record with value %s%n",
          value);

        final Callback callback = (metadata, exception) -> {
          out.format("Published with metadata: %s, error: %s%n",
            metadata, exception);
        };

        // publish the record, handling the metadata in the callback
        producer.send(new ProducerRecord<>(topic, key, value), callback);

        // wait x seconds before publishing another
        Thread.sleep(5000);
      }
    }
  }
}
