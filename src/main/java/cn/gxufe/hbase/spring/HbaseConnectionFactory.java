package cn.gxufe.hbase.spring;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HbaseConnectionFactory {

    protected static Configuration configuration;

    public static Connection getConnection() throws IOException {
        return ConnectionFactory.createConnection(configuration);
    }

}
