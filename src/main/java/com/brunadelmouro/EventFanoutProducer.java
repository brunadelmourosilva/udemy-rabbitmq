package com.brunadelmouro;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EventFanoutProducer {

    private static final String EVENTS = "events";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        //Open a connection AMQ and establish channel
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()){

            //Create a channel exchange "events"
            channel.exchangeDeclare(EVENTS, BuiltinExchangeType.FANOUT);

            int count = 1;
            while (true){
                //Send messages to fanout exchange "events"
                String message = "Event created " + count;

                System.out.println("Creating message: " + message);
                channel.basicPublish(EVENTS, "", null, message.getBytes());

                Thread.sleep(1000);

                count++;
            }
        }

    }
}
