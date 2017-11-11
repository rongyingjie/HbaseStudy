package cn.gxufe.hbase.coprocessor.observer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mortbay.util.ajax.JSON;

import java.io.IOException;
import java.util.*;

public class KafkaRegionObserver extends BaseRegionObserver {

    private String bootstrapServers;
    private String indexName;
    private String indexTopic;
    private KafkaProducer<String, String> procuder;
    @Override
    public void start(CoprocessorEnvironment e) throws IOException {
        Configuration configuration = e.getConfiguration();
        bootstrapServers = configuration.get("bootstrap.servers", "localhost:9092");
        indexName = configuration.get("index.name", "localhost:9092");
        indexTopic = configuration.get("index.topic", "localhost:9092");
        String acks = configuration.get("acks", "all");
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", acks);
        props.put("batch.size", 16384);
        props.put("linger.ms", 100);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        procuder = new KafkaProducer<>(props);
    }

    @Override
    public void stop(CoprocessorEnvironment e) throws IOException {
        if(procuder != null){
            procuder.close();
        }
    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        String indexId = new String(put.getRow());
        try {
            NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
            Map<String, Object> json = new HashMap<>();
            json.put("index_id",indexId);
            json.put("index_name",indexName);
            json.put("action","add");
            for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
                for (Cell cell : entry.getValue()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    json.put(key, value);
                }
            }
            String message = JSON.toString(json);
            ProducerRecord<String, String> msg = new ProducerRecord<String, String>(this.indexTopic, message);
            this.procuder.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit, Durability durability) throws IOException {
        String indexId = new String(delete.getRow());
        Map<String, Object> json = new HashMap<>();
        json.put("action","delete");
        json.put("index_name",indexName);
        json.put("index_id",indexId);
        String message = JSON.toString(json);
        ProducerRecord<String, String> msg = new ProducerRecord<String, String>(this.indexTopic, message);
        this.procuder.send(msg);
    }

}
