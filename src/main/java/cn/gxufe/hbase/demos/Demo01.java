package cn.gxufe.hbase.demos;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.List;

public class Demo01 {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "rongyingjie:2181");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table user = connection.getTable(TableName.valueOf("user"));
//        Put put = new Put("1".getBytes());
//        put.addColumn("f1".getBytes(),"name".getBytes(),"zhangsan".getBytes());
//        put.addColumn("f1".getBytes(),"age".getBytes(), Bytes.toBytes(20));
//        user.put(put);
//        System.out.println(connection);
//        connection.close();

        Result result = user.get(new Get("1".getBytes()));
        List<Cell> cells = result.listCells();
        for (Cell cell :    cells     ) {
            byte[] bytes = CellUtil.cloneValue(cell);
            String s1 = new String(CellUtil.cloneQualifier(cell));
            String s = new String(bytes);
            System.out.println(s+","+s1);
        }

    }
}
