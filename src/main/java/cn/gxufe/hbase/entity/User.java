package cn.gxufe.hbase.entity;

import cn.gxufe.hbase.core.HbaseColumn;
import cn.gxufe.hbase.core.HbaseTable;
import cn.gxufe.hbase.core.RowKey;

@HbaseTable("user")
public class User{
    @RowKey
    private String id;
    @HbaseColumn(name = "name",family = "f1")
    private String name;
    @HbaseColumn(name = "age",family = "f1")
    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public User(){

    }

    public User(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
