package cn.gxufe.hbase.spring;

import cn.gxufe.hbase.core.HbaseCurlRepositoryImpl;
import cn.gxufe.hbase.core.HbaseExecption;
import cn.gxufe.hbase.core.HbaseRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

public class HbaseFactoryBean<T> implements InitializingBean, FactoryBean<T> ,ApplicationContextAware {

    private String className;
    private  Class clz = null;
    private ApplicationContext applicationContext;

    public void setClassName(String className) throws ClassNotFoundException {
        this.className = className;
        clz = Class.forName(className);
    }
    public T getObject() throws Exception {
        if( clz.isInterface() ){
            Class[] interfaces = new Class[] { clz };
            HbaseCurlRepositoryImpl hbaseCurlRepository = new HbaseCurlRepositoryImpl(clz,applicationContext);
           return (T) Proxy.newProxyInstance(clz.getClassLoader(), interfaces, hbaseCurlRepository);
        }else {
            throw  new HbaseExecption("HbaseTable annotation should set in interface !");
        }
    }

    public Class<?> getObjectType() {
            return clz;
    }

    public boolean isSingleton() {
        HbaseRepository annotation = (HbaseRepository)clz.getAnnotation(HbaseRepository.class);
        return annotation.singleton();
    }

    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
