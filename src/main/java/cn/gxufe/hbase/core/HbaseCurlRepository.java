package cn.gxufe.hbase.core;

import java.util.List;

public interface HbaseCurlRepository<T> {
    T getById(java.io.Serializable id);
    T deleteById(java.io.Serializable id);
    void save(T t);
    void saveBatch(final List<T> list);
    List<T> findFromStartToEndRowKey(String startRowKey, String endRowKey);
}
