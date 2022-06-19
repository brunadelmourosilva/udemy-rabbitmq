package com.brunadelmouro;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class SportEventTopicConsumer {

    private static final String EXCHANGE = "sport-events";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Open connection
        Connection connection = connectionFactory.newConnection();

        //Establish channel
        Channel channel = connection.createChannel();

        //Declare exchange "events"
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

        //Create a queue and associate it to exchange "sport-events"
        String queueName = channel.queueDeclare().getQueue();

        //routing-key pattern -> country.sport.eventType
        // * -> word
        // # -> multiples word delimiter by .
        // tennis events -> *.tennis.*
        // events on Spanish -> es.#  / es.*.*
        // all the events -> #

        System.out.println("Enter with a routing-key: ");
        Scanner scanner = new Scanner(System.in);
        String routingKey  = scanner.nextLine();

        channel.queueBind(queueName, EXCHANGE, routingKey);

        //Create a subscription to a queue associated the exchange "sport-events"
        channel.basicConsume(queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Message received: " + messageBody);
                    System.out.println("Routing key: " + message.getEnvelope().getRoutingKey());
                },
                consumeTag -> {
                    System.out.println("Consumer: " + consumeTag + " canceled");
                });
    }
}
