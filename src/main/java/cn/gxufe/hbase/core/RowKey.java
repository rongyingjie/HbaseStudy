package cn.gxufe.hbase.core;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RowKey {
    String value() default "rowKey";
}
