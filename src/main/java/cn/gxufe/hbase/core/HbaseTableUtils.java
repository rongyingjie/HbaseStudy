package cn.gxufe.hbase.core;

import org.apache.hadoop.hbase.client.Table;

import java.util.Stack;

public final class HbaseTableUtils {
    private static ThreadLocal<Stack<Table>> tableStack = new ThreadLocal<Stack<Table>>(){
        protected Stack<Table> initialValue() {
            return new Stack<>();
        }
    };

    protected  static  void setTable(Table table){
        Stack<Table> tables = tableStack.get();
        tables.push(table);

    }

    public static Table getTable(){
        Stack<Table> tables = tableStack.get();
        if(tables.isEmpty()){
            return null;
        }else {
            Table table = tables.peek();
            return table;
        }
    }

    protected static void pop(){
        Stack<Table> tables = tableStack.get();
        if(!tables.isEmpty()){
            tables.pop();
        }
    }
}
