package cn.gxufe.hbase.repository;

import cn.gxufe.hbase.core.HbaseCurlRepository;
import cn.gxufe.hbase.core.HbaseRepository;
import cn.gxufe.hbase.entity.Student;
import cn.gxufe.hbase.repository.impl.StudentRepositoryImpl;

@HbaseRepository(customImpl = StudentRepositoryImpl.class)
public interface StudentRepository extends HbaseCurlRepository<Student> {

    Student getStudentByName(String name);

}
