package spendingManager.KAFKA.general;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class MessageSender {
	final static String TOPIC = "Hello from Kafka producer";

	public static void main(String[] argv) {
		Properties properties = new Properties();
		properties.put("metadata.broker.list", "localhost:2181");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		ProducerConfig producerConfig = new ProducerConfig(properties);
		kafka.javaapi.producer.Producer<String, String> producer = new kafka.javaapi.producer.Producer<String, String>(
				producerConfig);
		SimpleDateFormat sdf = new SimpleDateFormat();
		KeyedMessage<String, String> message = new KeyedMessage<String, String>(
				TOPIC, "Test message from java program "
						+ sdf.format(new Date()));
		producer.send(message);
		producer.close();
	}

}
