package cn.gxufe.hbase.repository.impl;

import cn.gxufe.hbase.core.HbaseTableUtils;
import cn.gxufe.hbase.entity.Student;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.stereotype.Component;

@Component
public class StudentRepositoryImpl {

    public Student getStudentByName(String name){
        Table table = HbaseTableUtils.getTable();
        System.out.println(table +"," + name);
        return new Student();
    }

}
