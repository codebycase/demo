package com.research.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;

@Configuration
public class SpringAmqpConfiguration {
  protected static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  static final String topicExchangeName = "rmq-test-exchange";
  public final static String queueName1 = "rmq_test_queue_one";
  public final static String queueName2 = "rmq_test_queue_two";
  
  @Bean
  Queue queue1() {
    return new Queue(queueName1, false);
  }

  @Bean
  Queue queue2() {
    return new Queue(queueName2, false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding1(Queue queue1, TopicExchange exchange) {
    return BindingBuilder.bind(queue1).to(exchange).with("foo.one.*");
  }

  @Bean
  Binding binding2(Queue queue2, TopicExchange exchange) {
    return BindingBuilder.bind(queue2).to(exchange).with("foo.two.*");
  }

  /*
  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setAddresses(addresses);
    connectionFactory.setVirtualHost(virtualHost);
    connectionFactory.setUsername(virtualHost); // Same with vhost by default
    connectionFactory.setPassword(virtualHost); // Same with vhost by default
    // Demo correlation confirm
    connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
    connectionFactory.setPublisherReturns(true);
    return connectionFactory;
  }
  */

  /* Alternative way to set up a message listener */

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName1);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter() {
    return new MessageListenerAdapter(new SimpleMessageHandler());
  }

  public class SimpleMessageHandler {
    public void handleMessage(String message) {
      logger.info("Handler received message from queue={}: {}", queueName1, message);
    }
  }
}
