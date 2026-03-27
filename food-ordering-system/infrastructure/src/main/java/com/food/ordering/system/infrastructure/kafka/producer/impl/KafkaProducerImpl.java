package com.food.ordering.system.infrastructure.kafka.producer.impl;

import java.io.Serializable;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.food.ordering.system.infrastructure.exceptions.KafkaProducerException;
import com.food.ordering.system.infrastructure.kafka.producer.KafkaProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> 
implements KafkaProducer<K, V>{
	
	private final KafkaTemplate<K, V> kafkaTemplate;
	
	public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void send(String topicName, K key, V message) {
		log.info("Sending message={} to topic={}", message, topicName);
		
		try {
			kafkaTemplate.send(topicName, message);
		} catch (KafkaException e) {
			log.info("Error on kafka producer with key:{}, message:{} and exception: {}", key, message, e.getMessage());
			throw new KafkaProducerException("Error on producer with key: "+key+" and message: "+message);
		}
		
	}
	
	

}
