package cn.gxufe.hbase.spring;

import cn.gxufe.hbase.core.HbaseExecption;

import java.lang.reflect.*;

public class HbaseInterfaceProxy<T> implements InvocationHandler {

    private Class<T> innerInterface;
    private Class idClass;
    private Class entityClass;

    public HbaseInterfaceProxy(Class<T> innerInterface){
        this.innerInterface = innerInterface;
        Type[] genericInterfaces = this.innerInterface.getGenericInterfaces();
        if(genericInterfaces == null || genericInterfaces.length == 0){
            throw new HbaseExecption(" Repository 必须配置ID和实体的泛型参数 ");
        }
        Type[] actualTypeArguments = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments();
        if(actualTypeArguments == null || actualTypeArguments.length != 2){
            throw new HbaseExecption(" Repository 必须配置ID和实体的泛型参数 ");
        }
        idClass = (Class) actualTypeArguments[0];
        entityClass = (Class) actualTypeArguments[1];
        init();
    }

    private void init(){
        System.out.println("idClass = " + idClass+",entityClass = " + entityClass);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("=============HbaseInterfaceProxy============" + innerInterface.getSimpleName() +"method = " + method.getName());
        if( "toString".equals(method.getName())){
            return this.toString();
        }else {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> innerInterface) {
        ClassLoader classLoader = innerInterface.getClassLoader();
        Class[] interfaces = new Class[] { innerInterface };
        HbaseInterfaceProxy proxy = new HbaseInterfaceProxy(innerInterface);
        return  (T)Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }

}
