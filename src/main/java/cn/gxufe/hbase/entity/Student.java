package cn.gxufe.hbase.entity;

import cn.gxufe.hbase.core.HbaseColumn;
import cn.gxufe.hbase.core.HbaseTable;

@HbaseTable("student")
public class Student {
    @HbaseColumn(name = "id",family = "f1")
    private String id;
    @HbaseColumn(name = "name",family = "f1")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
