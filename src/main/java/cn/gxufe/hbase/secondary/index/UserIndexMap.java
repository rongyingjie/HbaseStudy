package cn.gxufe.hbase.secondary.index;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;


import java.io.IOException;

/**
 *
 * 事例说明 ：
 *  user 有两个字段，id(rowKey) 和 name
 *
 *  构建索引：
 *      name（rowKwy）和 value(id)
 *
 *  大部分是硬编码完成
 *
 * 利用的hbase的命令提交作业
 *      bin/hbase cn.gxufe.hbase.secondary.index.UserIndexMap user
 *
 *
 * 二级索引构建方案 ：
 *      1、map reduce 实现
 *      2、IHbase
 *          利用数据刷到磁盘的过程，构建索引
 *      3、ITHbase
 *
 *      4、协处理器：
 *          实现案例：KafkaRegionObserver
 *          利用prePut,postPut,preDelete 监控 hbase 的 curd
 *
 */
public class UserIndexMap {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        String tableName = args[0];
        conf.set("tableName",tableName);
        Job job = new Job(conf,tableName);
        job.setJarByClass(UserIndexMap.class);
        job.setMapperClass(UserNameMapper.class);
        job.setNumReduceTasks(0);
        job.setInputFormatClass(TableInputFormat.class);
        job.setOutputFormatClass(MultiTableOutputFormat.class);
        Scan scan = new Scan();
        TableMapReduceUtil.initTableMapperJob(tableName,scan,UserNameMapper.class,ImmutableBytesWritable.class,Put.class,job);
        job.waitForCompletion(true);
    }
}

class UserNameMapper extends TableMapper<ImmutableBytesWritable,Put>{
    private String tableName;
    private byte[] secondaryIndexName;
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        tableName = configuration.get("tableName");
        secondaryIndexName = Bytes.toBytes(tableName+"_index");
    }
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        byte[] secondaryIndexKey = value.getValue("f1".getBytes(), "name".getBytes());
        Put put = new Put(secondaryIndexKey);
        put.addColumn("f1".getBytes(),"value".getBytes(),key.get());
        context.write(new ImmutableBytesWritable(secondaryIndexName),put);
    }
}
