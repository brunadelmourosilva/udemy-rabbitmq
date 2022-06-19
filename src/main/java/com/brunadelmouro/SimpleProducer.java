package com.brunadelmouro;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SimpleProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        String message = "Hello";


        //Open a new AMQ connection and Establish channel
        ConnectionFactory connectionFactory = new ConnectionFactory();

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            //Create a queue
            String queueName = "first-queue";
            channel.queueDeclare(queueName, false, false, false, null);

            //Send a message to exchange
            channel.basicPublish("", queueName, null, message.getBytes());

        }
    }
}
