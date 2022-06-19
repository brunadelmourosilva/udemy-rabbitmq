package com.brunadelmouro;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class SimpleConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //Open a connection AMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();

        //Establish a channel
        Channel channel = connection.createChannel();

        //Declare the queue "first-queue"
        channel.queueDeclare("first-queue", false, false, false, null);

        //Create a subscription to a queue using the command "Basic.consume"
        channel.basicConsume("first-queue",
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());

                    System.out.println("Message: " + messageBody);
                    System.out.println("Exchange: " + message.getEnvelope().getExchange());
                    System.out.println("Routing key: " + message.getEnvelope().getRoutingKey());
                    System.out.println("Delivery key: " + message.getEnvelope().getDeliveryTag());
                },
                consumeTag -> {
                    System.out.println("Consumer: " + consumeTag + " canceled");
                });
    }
}
