package cn.gxufe.hbase.spring;

import cn.gxufe.hbase.core.HbaseExecption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class HbaseFactoryBean<T> implements InitializingBean, FactoryBean<T> {

    private String className;

    public void setClassName(String className) {
        this.className = className;
    }

    public T getObject() throws Exception {
        Class clz = Class.forName(className);
        if( clz.isInterface() ){
           return (T)HbaseInterfaceProxy.newInstance(Class.forName(className));
        }else {
            throw  new HbaseExecption("HbaseTable annotation should set in interface !");
        }
    }

    public Class<?> getObjectType() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {

    }

}
