package cn.gxufe.hbase.repository;

import cn.gxufe.hbase.core.HbaseCurlRepository;
import cn.gxufe.hbase.core.HbaseRepository;
import cn.gxufe.hbase.entity.User;

@HbaseRepository
public interface UserRepository extends HbaseCurlRepository<String,User> {

}
