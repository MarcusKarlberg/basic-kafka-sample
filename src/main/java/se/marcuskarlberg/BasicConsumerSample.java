package se.marcuskarlberg;

import static java.lang.System.*;

import java.time.*;
import java.util.*;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

public final class BasicConsumerSample {
  public BasicConsumerSample() {}

  public void runConsumer() {
    final var topic = "getting-started";

    final Map<String, Object> config =
      Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
        ConsumerConfig.GROUP_ID_CONFIG, "basic-consumer-sample",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

    try (var consumer = new KafkaConsumer<String, String>(config)) {
      consumer.subscribe(Set.of(topic));

      // Kafka consumers are designed to run continuously to consume a stream of messages as they arrive
      //The loop keeps polling repeatedly, ensuring you get all new messages over time
      //This is necessary to keep the consumer group alive and maintain offset commits
      while (true) {
        final var records = consumer.poll(Duration.ofMillis(100));
        for (var record : records) {
          out.format("Got record with value %s%n", record.value());
        }
        consumer.commitAsync();
      }
    }
  }
}
