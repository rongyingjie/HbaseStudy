package cn.gxufe.hbase.core;

public interface HbaseCurlRepository<ID extends java.io.Serializable,T> {
    T getById(ID id);
    T deleteById(ID id);
}
