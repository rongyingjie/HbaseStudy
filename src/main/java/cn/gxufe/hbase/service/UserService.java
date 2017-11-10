package cn.gxufe.hbase.service;

import cn.gxufe.hbase.core.HbaseCurlRepository;
import cn.gxufe.hbase.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private HbaseCurlRepository<User> hbaseCurlRepository;

    public User getUserById(String id){
       return hbaseCurlRepository.getById(id);
    }

    public User deleteById(String id){
        return hbaseCurlRepository.deleteById(id);
    }

}
