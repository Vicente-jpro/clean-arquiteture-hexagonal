package com.food.ordering.system.infrastructure.kafka.producer;

import java.io.Serializable;

import org.apache.avro.specific.SpecificRecordBase;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase>{

	void send(String topicName, K key, V message);
}
