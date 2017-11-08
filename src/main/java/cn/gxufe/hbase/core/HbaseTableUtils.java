package cn.gxufe.hbase.core;

import org.apache.hadoop.hbase.client.Table;

public final class HbaseTableUtils {
    private ThreadLocal<Table> tableThreadLocal = new ThreadLocal<Table>();
    protected void setTable(Table table){
        tableThreadLocal.set(table);
    }
    public Table getTable(){
        return tableThreadLocal.get();
    }
}
