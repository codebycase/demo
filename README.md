# Getting Started


## Sleuth RabbitMQ

- Run local RabbitMQ docker

```
docker run --rm -it --hostname my-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

- Run local Zipkin docker

```
docker run -d -p 9411:9411 openzipkin/zipkin
```

- Now you can run DemoApplication.java

- As observed, the trace will be enabled when the log level is set to DEBUG


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.9/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.9/maven-plugin/reference/html/#build-image)
* [Testcontainers RabbitMQ Module Reference Guide](https://www.testcontainers.org/modules/rabbitmq/)
* [Sleuth](https://docs.spring.io/spring-cloud-sleuth/docs/current/reference/htmlsingle/spring-cloud-sleuth.html)
* [Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-overview-introducing)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/docs/2.6.9/reference/htmlsingle/#messaging.amqp)
* [Testcontainers](https://www.testcontainers.org/)

### Guides
The following guides illustrate how to use some features concretely:

* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)


#### with brave

2022-07-26 11:20:21.771 DEBUG [,,] 19627 --- [           main] o.s.amqp.rabbit.core.RabbitTemplate      : Publishing message [(Body:'key-1;value-1' MessageProperties [headers={b3=57538165580910d3-57538165580910d3-0}, contentType=text/plain, contentEncoding=UTF-8, contentLength=13, deliveryMode=PERSISTENT, priority=0, deliveryTag=0])] on exchange [rmq-test-exchange], routingKey = [foo.one.baz]

2022-07-26 11:20:21.777 DEBUG [,,] 19627 --- [    container-1] o.s.a.r.listener.BlockingQueueConsumer   : Received message: (Body:'key-1;value-1' MessageProperties [headers={b3=57538165580910d3-57538165580910d3-0}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=rmq-test-exchange, receivedRoutingKey=foo.one.baz, deliveryTag=1, consumerTag=amq.ctag-PB-GQ9HB6MOWVVaPVqawTg, consumerQueue=rmq_test_queue_one])

#### with otel

2022-07-26 11:17:05.826 DEBUG [,,] 19519 --- [           main] o.s.amqp.rabbit.core.RabbitTemplate      : Publishing message [(Body:'key-1;value-1' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=13, deliveryMode=PERSISTENT, priority=0, deliveryTag=0])] on exchange [rmq-test-exchange], routingKey = [foo.one.baz]

2022-07-26 11:17:05.829 DEBUG [,,] 19519 --- [    container-1] o.s.a.r.listener.BlockingQueueConsumer   : Received message: (Body:'key-1;value-1' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=rmq-test-exchange, receivedRoutingKey=foo.one.baz, deliveryTag=2, consumerTag=amq.ctag-SaKuyTMGuohNVL4z_ZGHuQ, consumerQueue=rmq_test_queue_one])
