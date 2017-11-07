package cn.gxufe.hbase.repository;

import cn.gxufe.hbase.core.HbaseCurlRepository;
import cn.gxufe.hbase.core.HbaseRepository;
import cn.gxufe.hbase.entity.Student;
@HbaseRepository
public interface StudentRepository extends HbaseCurlRepository<String,Student> {
}
