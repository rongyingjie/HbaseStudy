package cn.gxufe.hbase.core;

import org.apache.hadoop.hbase.client.Table;

public final class HbaseTableUtils {
    private static ThreadLocal<Table> tableThreadLocal = new ThreadLocal<Table>();
    protected  static  void setTable(Table table){
        tableThreadLocal.set(table);
    }
    public static Table getTable(){
        return tableThreadLocal.get();
    }
}
