package cn.gxufe.hbase.repository.impl;

import cn.gxufe.hbase.core.HbaseCurlRepository;
import cn.gxufe.hbase.core.HbaseTableUtils;
import cn.gxufe.hbase.entity.Student;
import cn.gxufe.hbase.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentRepositoryImpl {

    @Autowired
    private HbaseCurlRepository<Student> studentRepository;

    @Autowired
    private HbaseCurlRepository<User> userRepository;

    public Student getStudentByName(String name){
        System.out.println(new String(HbaseTableUtils.getTable().getName().toBytes()));
        User user = userRepository.getById("1");
        System.out.println(user);
        Student student = studentRepository.getById("1");
        System.out.println(new String(HbaseTableUtils.getTable().getName().toBytes()));
        System.out.println(student);
        return new Student();
    }

}
