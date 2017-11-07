package cn.gxufe.hbase.core;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HbaseColumn {
    String name();
    String family();
}
