package cn.gxufe.hbase.spring;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class HbaseBeanScannerConfigure  implements BeanFactoryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private String[] basePackages;
    private Configuration conf;
    public HbaseBeanScannerConfigure(Configuration conf,String... basePackages){
        if( basePackages == null || basePackages.length == 0 ){
            throw new RuntimeException(" basePackages can not be null !!! ");
        }
        this.conf = conf;
        this.basePackages = basePackages;
    }
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        HbaseConnectionFactory.configuration = conf;
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(HbaseConnectionFactory.class,"getConnection");
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        defaultListableBeanFactory.registerBeanDefinition("connection",beanDefinitionBuilder.getRawBeanDefinition());
        HbaseScanner hbaseScanner = new HbaseScanner((BeanDefinitionRegistry) beanFactory);
        hbaseScanner.setResourceLoader(this.applicationContext);
        hbaseScanner.scan(this.basePackages);
    }

}
