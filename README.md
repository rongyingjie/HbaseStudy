# HbaseStudy 包含的内容
 - hbase 基本的crud
 - hbase与map reduce 整合 
 - hbase的协处理器，监控curd,再将数据写入kafka(也可以es等)
 - hbase的spring整合，手动实现类似spring data的功能，自定义注解和包扫描创建repository

# Hbase的rowkey设计
 - rowkey 划分区间（字典序）进行存储，比如 1-5 开始的存储在 region01,6-9 开始的存储在 region02
 - 按照rowkey存储，那么每个区间的数据量和读写频率，就会直接影响性能，尽可能的保证每个区间的数据量相差不大
 - 表的创建，默认是不会只有一个region,也可以自定义预分区
 - hbase的负载均衡，后台的守护进程会定时扫描表的大小，决定是否会分裂region
