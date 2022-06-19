package com.brunadelmouro;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SportEventTopicProducer {

    private static final String EXCHANGE = "sport-events";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        //Open a connection AMQ and establish channel
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()){

            //Create a channel exchange "sport-events"
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

            //countries
            List<String> countries = Arrays.asList("es", "br", "usa");

            //sport
            List<String> sports = Arrays.asList("soccer", "ping-pong", "tennis");

            //event type
            List<String> eventTypes = Arrays.asList("in live", "news");

            int count = 1;
            while (true){
                //random list
                shuffleList(countries, sports, eventTypes);
                String country = countries.get(0);
                String sport = sports.get(0);
                String eventType = eventTypes.get(0);

                //routing-key pattern -> country.sport.eventType
                String routingKey = country + "." + sport + "." + eventType;

                //Send messages to fanout exchange "sport-events"
                String message = "Event created " + count;

                System.out.println("Creating message (" + routingKey + "): " + message);
                channel.basicPublish(EXCHANGE, "", null, message.getBytes());

                Thread.sleep(1000);

                count++;
            }
        }

    }

    private static void shuffleList(List<String> countries, List<String> sports, List<String> eventTypes) {
        Collections.shuffle(countries);
        Collections.shuffle(sports);
        Collections.shuffle(eventTypes);
    }
}
