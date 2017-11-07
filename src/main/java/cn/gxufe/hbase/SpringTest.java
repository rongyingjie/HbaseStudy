package cn.gxufe.hbase;

import cn.gxufe.hbase.repository.StudentRepository;
import cn.gxufe.hbase.repository.UserRepository;
import cn.gxufe.hbase.spring.HbaseBeanScannerConfigure;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
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

        UserRepository userRepository = applicationContext.getBean(UserRepository.class);

        System.out.println(userRepository);

        StudentRepository studentRepository = applicationContext.getBean(StudentRepository.class);

        System.out.println(studentRepository);

        Connection connection = applicationContext.getBean(Connection.class);

        System.out.println(connection);


    }


}
