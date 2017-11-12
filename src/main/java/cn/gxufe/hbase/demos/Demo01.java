package cn.gxufe.hbase.demos;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.WhileMatchFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Demo01 {

    private Connection connection;
    @Before
    public void before() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "rongyingjie:2181");
        connection = ConnectionFactory.createConnection(conf);
    }

    @Test
    public void get() throws IOException {
        Table user = connection.getTable(TableName.valueOf("user"));
        Result result = user.get(new Get("1".getBytes()));
        println(result);
    }

    @Test
    public void scan()throws IOException{
        Table user = connection.getTable(TableName.valueOf("user"));
        Scan scan = new Scan();
        ResultScanner scanner = user.getScanner(scan);
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result result = iterator.next();
            println(result);
        }
    }


    @Test
    public void delete() throws IOException{
        Table user = connection.getTable(TableName.valueOf("user"));
        Delete delete = new Delete("2".getBytes());
        user.delete(delete);
    }

    @Test
    public void rowFilter() throws IOException{
        Table user = connection.getTable(TableName.valueOf("user"));
        Scan scan = new Scan();
        scan.setStartRow("1".getBytes());
        scan.setStopRow("2".getBytes());
        ResultScanner scanner = user.getScanner(scan);
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result result = iterator.next();
            println(result);
        }

    }

    @After
    public void after() throws IOException {
        connection.close();
    }

    private void println(Result result){
        List<Cell> cells = result.listCells();
        System.out.println("==================");
        for (Cell cell  :    cells     ) {
            byte[] bytes = CellUtil.cloneValue(cell);
            String s1 = new String(CellUtil.cloneQualifier(cell));
            String s = new String(bytes);
            System.out.println(s+","+s1);
        }
    }
}
