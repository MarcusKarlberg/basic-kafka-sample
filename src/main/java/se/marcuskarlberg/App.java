package se.marcuskarlberg;

import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 *
 */
@Slf4j
public class App
{
  public static void main(String[] args) throws InterruptedException {
    BasicConsumerSample consumerSample = new BasicConsumerSample();
    BasicProducerSample producerSample = new BasicProducerSample();

    Thread consumerThread = new Thread(consumerSample::runConsumer);
    Thread producerThread = new Thread(() -> {
      try {
        producerSample.runProducer();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    consumerThread.start();
    log.info("Running consumer");

    producerThread.start();
    log.info("Running producer");
  }
}
