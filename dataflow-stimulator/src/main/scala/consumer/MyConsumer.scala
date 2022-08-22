package consumer

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util
import java.util.Properties
//import scala.jdk.CollectionConverters.IterableHasAsScala

object MyConsumer {
 val configs = ConfigFactory.load()
 val envProps = configs.getConfig("dev") // args(0) = "dev"
 val properties = new Properties()
 // connected Kafka cluster
 properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, envProps.getString("bootstrap.server"))
 // control consuming speed
 properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, envProps.getString("consumerSpeed"))
 // Key, Value's serializer
 properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
 properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
 // consumer group
 properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1")
 val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](properties)
 consumer.subscribe(util.Arrays.asList("ratings"))

 // consuming speed controller
//  while (true) {
//    val consumerRecords = consumer.poll(Duration.ofMillis(2000)).asScala
//    for (consumerRecord <- consumerRecords) {
//      println("topic: " + consumerRecord.topic + ", value: " + consumerRecord.value)
//    }
//  }
}
