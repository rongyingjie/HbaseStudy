package cn.gxufe.hbase.core;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HbaseTable {
    String value();

}
