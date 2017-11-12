package cn.gxufe.hbase.entity;

import cn.gxufe.hbase.core.HbaseColumn;
import cn.gxufe.hbase.core.HbaseTable;
import cn.gxufe.hbase.core.RowKey;

@HbaseTable("student")
public class Student {
    @RowKey
    private String id;
    @HbaseColumn(name = "name",family = "f1")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Student(){}

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
