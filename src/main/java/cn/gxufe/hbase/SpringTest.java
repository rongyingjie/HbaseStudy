package cn.gxufe.hbase;

import cn.gxufe.hbase.repository.UserRepository;
import cn.gxufe.hbase.spring.HbaseBeanScannerConfigure;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SpringTest {

    @Bean
    public HbaseBeanScannerConfigure getHbaseBeanScannerConfigure(){
       return new HbaseBeanScannerConfigure("cn.gxufe.hbase.repository");

    }

    public static void main(String[] args) throws IOException {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringTest.class);

        UserRepository bean = applicationContext.getBean(UserRepository.class);


        Connection connection = applicationContext.getBean(Connection.class);

        System.out.println(connection);


    }


}
