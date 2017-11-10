package cn.gxufe.hbase;

import cn.gxufe.hbase.core.HbaseTableUtils;
import cn.gxufe.hbase.entity.User;
import cn.gxufe.hbase.repository.StudentRepository;
import cn.gxufe.hbase.repository.UserRepository;
import cn.gxufe.hbase.service.UserService;
import cn.gxufe.hbase.spring.HbaseBeanScannerConfigure;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan
public class SpringTest {

    @Bean
    public HbaseBeanScannerConfigure getHbaseBeanScannerConfigure(){
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "rongyingjie:2181");
        String [] basePackages = {"cn.gxufe.hbase.repository"};
       return new HbaseBeanScannerConfigure(conf,basePackages);
    }

    public static void main(String[] args) throws IOException {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringTest.class);

//        UserService userService = applicationContext.getBean(UserService.class);
//
//
//        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
//
//
//        userRepository.save(new User("1","zhangsan",20));
//
//
//        User user = userService.getUserById("1");
//
//        System.out.println(user);


        StudentRepository studentRepository = applicationContext.getBean(StudentRepository.class);

        studentRepository.getStudentByName("a");

        Table table = HbaseTableUtils.getTable();

        System.out.println(table);

        //        StudentRepository studentRepository = applicationContext.getBean(StudentRepository.class);
//

    //    System.out.println(studentRepository.getStudentByName("abc"));


    }


}
