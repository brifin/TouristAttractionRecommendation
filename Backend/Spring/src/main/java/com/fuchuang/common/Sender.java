
package com.fuchuang.common;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
public class Sender {
    public static final String broker_list = "47.107.38.208:9092";
    public static final String topic = "locations";
    static public void send(int id,double preLatitude,double preLongitude,String nLai,String nLon){
        String str=preLatitude+","+preLongitude+","+nLai+","+nLon;
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer=new KafkaProducer<String,String>(props);
        Message message=new Message();
        message.setId(id);
        message.setMessage(str);
        ProducerRecord record=new ProducerRecord<String,String>(topic,null,null,GsonUtil.toJson(message));
        producer.send(record);
        System.out.println("send!");
        producer.flush();
    }
}