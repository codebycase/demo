package com.research.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class SleuthTraceWithSpringRabbit {
  private static final Logger log = LoggerFactory.getLogger(SleuthTraceWithSpringRabbit.class);

  @Autowired
  Tracer sleuthTracer;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = SpringAmqpConfiguration.queueName2)
  public void listen(Message message) {
    log.info("Listener received message from queue={}: {}", SpringAmqpConfiguration.queueName2, message);
  }

  /**
   * Use Spring Rabbit Template and Listener
   */
  public void testSleuthOtelTraceWithSpringRabbit() throws Exception {
    // Add 3 messages with auto generated spans
    int messagesCount = 3;
    for (int i = 0; i < messagesCount; i++) {
      rabbitTemplate.convertAndSend(SpringAmqpConfiguration.topicExchangeName, "foo.one.baz", "key-" + i + ";" + "value-" + i);
    }

    // Add one message with an extra span
    Span span = sleuthTracer.nextSpan().name("rabbit.send.add.span");
    try (Tracer.SpanInScope ws = sleuthTracer.withSpan(span.start())) {
      Integer messageId = 1234;
      log.info("Add span for traceId={}, messageId={}", sleuthTracer.currentSpan().context().traceId(), messageId);
      String message = "key-span-" + messageId + ";" + "value-span-" + messageId;
      CorrelationData correlationData = new CorrelationData("Correlation for message " + messageId);
      rabbitTemplate.convertAndSend(SpringAmqpConfiguration.topicExchangeName, "foo.two.baz", message, correlationData);
      correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
        @Override
        public void onSuccess(CorrelationData.Confirm result) {
          log.info("Confirm received from queue={}, ack={}", SpringAmqpConfiguration.queueName2, result.isAck());
          span.end();
        }

        @Override
        public void onFailure(Throwable ex) {
          log.info("Failed to send a message to queue=" + SpringAmqpConfiguration.queueName2, ex);
          span.end();
        }
      });
      // Blocking to ensure that we push all the spans
      correlationData.getFuture().get();
    } finally {
      span.end();
    }
  }
}
