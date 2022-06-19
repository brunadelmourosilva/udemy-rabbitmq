package com.brunadelmouro;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class EventFanoutConsumer {

    private static final String EVENTS = "events";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Open connection
        Connection connection = connectionFactory.newConnection();

        //Establish channel
        Channel channel = connection.createChannel();

        //Declare exchange "events"
        channel.exchangeDeclare(EVENTS, BuiltinExchangeType.FANOUT);

        //Create a queue and associate it to exchange "events"
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EVENTS, "");

        //Create a subscription to a queue associated the exchange "events"
        channel.basicConsume(queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());

                    System.out.println("Message received: " + messageBody);
                },
                consumeTag -> {
                    System.out.println("Consumer: " + consumeTag + " canceled");
                });
    }
}
