package me.togo.demo.disruptor.height.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class MultiTest {

    @Test
    public void testMulti() throws InterruptedException {

//        1. 构建ringBuffer
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                new EventFactory<Order>() {
                    @Override
                    public Order newInstance() {
                        return new Order();
                    }
                }, 1024 * 1024, new YieldingWaitStrategy());

//        2.创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();


//        3.实例化多消费者
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C_" + i);
        }

//        4.构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);

//        5.设置多个Consumer的sequence序号，用于统计每一个consumer的进度，设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

//        6.启动workpool 工作起来
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 100; i++) {
            Producer producer = new Producer(ringBuffer);
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 100; j++) {
                    producer.sendData(UUID.randomUUID().toString());
                }
            }).start();
        }
        Thread.sleep(2000);
        System.out.println("--------------线程准备完毕，准备生产数据----------------");
        latch.countDown();
        Thread.sleep(8000);
        System.out.println("消费总数：" + Consumer.getCount());
    }
}
