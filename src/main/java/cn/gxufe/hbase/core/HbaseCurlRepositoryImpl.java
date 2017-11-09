package cn.gxufe.hbase.core;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;

public class HbaseCurlRepositoryImpl implements InvocationHandler ,HbaseCurlRepository{
    private Class entityClass;
    private String tableName;
    private HashMap<String,Method[]> methods = null;
    private HashMap<String,Class> fieldsType = null;
    private HashMap<String,String> familys = null;
    private Class interInterface;
    private ApplicationContext applicationContext;
    private Class customImpl = null;

    private HashMap<String,Method> customImplMethod = new HashMap<>(0);
    private HashMap<String,Object> customImplBean = new HashMap<>(0);

    private String rowKeyName = null;

    public HbaseCurlRepositoryImpl(Class interInterface,ApplicationContext applicationContext){
        this.interInterface = interInterface;
        this.applicationContext = applicationContext;
        HbaseRepository annotation = (HbaseRepository)this.interInterface.getAnnotation(HbaseRepository.class);
        this.customImpl = annotation.customImpl();
        Type[] genericInterfaces = interInterface.getGenericInterfaces();
        if(genericInterfaces == null || genericInterfaces.length == 0){
            throw new HbaseExecption(" Repository 必须实体的泛型参数 ");
        }
        Type[] actualTypeArguments = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments();
        if(actualTypeArguments == null || actualTypeArguments.length != 1 ){
            throw new HbaseExecption(" Repository 必须实体的泛型参数 ");
        }
        entityClass = (Class) actualTypeArguments[0];
        this.init();
    }

    private void init(){
        HbaseTable hbaseTable = (HbaseTable)entityClass.getAnnotation(HbaseTable.class);
        this.tableName = hbaseTable.value();
        Field[] declaredFields = this.entityClass.getDeclaredFields();
        if( declaredFields == null || declaredFields.length == 0 ){
           throw new HbaseExecption("tableName = " + tableName +  ", fields is null !!");
        }
        methods = new HashMap<>(declaredFields.length);
        fieldsType = new HashMap<>(declaredFields.length);
        familys = new HashMap<>(1);
        for (Field field : declaredFields ) {
            HbaseColumn hbaseColumn = field.getAnnotation(HbaseColumn.class);
            RowKey rowKey = field.getAnnotation(RowKey.class);
            Class<?> type = field.getType();
            String name = field.getName();
            if(  rowKey != null ){
                if(rowKeyName == null){
                    this.rowKeyName = name;
                }else {
                     throw new HbaseExecption(" has to many rowKey !! rowKeyName="+name + ",rowKeyType="+type.getSimpleName());
                }
                setMethods(type,name);
            }else if(hbaseColumn != null) {
                setMethods(type,name);
                familys.put(name,hbaseColumn.family());
            }
        }

        if( this.customImpl != HbaseCurlRepository.class ){
            Object bean = this.applicationContext.getBean(this.customImpl);
            Class aClass = bean.getClass();
            Method[] interInterfaceMethods = this.interInterface.getMethods();
            if( interInterfaceMethods != null && interInterfaceMethods.length > 0 ){
                for (Method m  :  interInterfaceMethods ) {
                    Method method = null;
                    Class[] genericParameterTypes = m.getParameterTypes();
                    String methodName = m.getName();
                    try {
                        method = aClass.getDeclaredMethod(methodName, genericParameterTypes);
                    } catch (NoSuchMethodException e) {

                    }
                    if(method != null){
                        customImplMethod.put(m.getName(),method);
                        customImplBean.put(m.getName(),bean);
                    }
                }
            }
        }
    }


    private void setMethods(Class<?> type,String name){
        try {
            Method getMethod =  this.entityClass.getMethod( "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length()));
            Method setMethod =  this.entityClass.getMethod( "set"+name.substring(0,1).toUpperCase()+name.substring(1,name.length()),type);
            methods.put(name,new Method[]{getMethod,setMethod});
            fieldsType.put(name,type);
        }catch (NoSuchMethodException e){
            throw new RuntimeException(e);
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        Method customMethod= this.customImplMethod.get(name);
        if(customMethod != null ){
            Connection connection = applicationContext.getBean(Connection.class);
            Table table = connection.getTable(TableName.valueOf(this.tableName));
            HbaseTableUtils.setTable(table);
            try {
                return customMethod.invoke( this.customImplBean.get(name),args);
            }catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                table.close();
            }
        }
        switch (name) {
            case "getById":
                return getById((Serializable)args[0]);
            case "deleteById":
                return deleteById((Serializable)args[0]);
            case "save":
                save(args[0]);
                return null;
            case "saveBatch":
                saveBatch((List) args[0]);
                return null;
            case "findFromStartToEndRowKey":
               return findFromStartToEndRowKey( args[0].toString(),args[1].toString());
            default:
                System.out.println("default");
                   break;
        }
        if(method.getName().equals("toString")){
            return this.toString();
        }
        return null;
    }

    public Object getById(Serializable id) {
        Connection connection = applicationContext.getBean(Connection.class);
        Object entity;
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(this.tableName));
            entity = entityClass.newInstance();
            Get get = new Get(id.toString().getBytes());
            Result result = table.get(get);
            if(result == null){
                return null;
            }
            String row = new String(result.getRow());
            Method[] methods = this.methods.get(this.rowKeyName);
            methods[1].invoke(entity,row);
            List<Cell> cells = result.listCells();
            for (Cell cell :    cells     ) {
                byte[] bytes = CellUtil.cloneValue(cell);
                String fname = new String(CellUtil.cloneQualifier(cell));
                Class aClass = this.fieldsType.get(fname);
                if(aClass == null){
                    continue;
                }
                Object value=null;
                Method[] method = this.methods.get(fname);
                switch (aClass.getName()){
                    case "java.lang.String":
                        value = new String(bytes);
                        break;
                    case "java.lang.Integer":
                        value = Bytes.toInt(bytes);
                        break;
                }
                method[1].invoke(entity,value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            this.closeTable(table);
        }
        return entity;
    }

    private void closeTable( Table table ){
        try {
            if( table != null){
                table.close();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Object deleteById(Serializable id) {
        return null;
    }

    public void save(Object o) {

    }

    public void saveBatch(List list) {

    }

    public List findFromStartToEndRowKey(String startRowKey, String endRowKey) {
        return null;
    }
}
