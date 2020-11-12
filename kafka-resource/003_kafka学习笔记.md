### Kafka学习笔记

------



- ###### kafka生产者重要的参数：

  ```shell
  1. acks：acks 是生产者客户端中一个非常重要的参数，它涉及消息的可靠性和吞吐量之间的权衡。acks有三个取值 默认为1 ， 0 ， -1 or all ，面试题：
  -----------------------------------------------------------------------
  A1: 说一下acks 3个取值代表什么含义，分别适用于什么样的应用场景？
  Q1:
  A2: acks= -1 or acks=all 是不是一定能够保障消息的可靠性呢？
  Q2: min.insync.replicas = 2
  
  2. 其他核心参数：
  ## max.request.size：
  该参数用来限制生产者客户端能发送的消息的最大值
  ## retries和retry.backoff.msretries：
  重试次数和重试间隔，默认100
  ## compression.type：
  这个参数用来指定消息的压缩方式，默认值为“none”，可选配置：“gzip”“snappy”和“lz4”
  ## connections.max.idle.ms ：
  这个参数用来指定在多久之后关闭限制的连接，默认值是540000（ms），即9分钟；
  ## linger.ms：
  这个参数用来指定生产者发送 ProducerBatch 之前等待更多消息（ProducerRecord）加入ProducerBatch 的时间，默认值为 0
  ## batch.size：
  累计多少条消息，则一次进行批量发送；
  ## buffer.memory：
  缓存提升性能参数，默认为32M
  ## receive.buffer.bytes：
  这个参数用来设置Socket接收消息缓冲区（SO_RECBUF）的大小，默认值为32768（B），即32KB；
  ## send.buffer.bytes：
  这个参数用来设置Socket发送消息缓冲区（SO_SNDBUF）的大小，默认值为131072（B），即128KB。
  ## request.timeout.ms：
  这个参数用来配置Producer等待请求响应的最长时间，默认值为30000（ms）
  
  ```

- 1

- 