package me.togo.demo.disruptor.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class OrderEventTest {


    //    参数的准备
    private EventFactory eventFactory = new OrderEventFactory();
    private int ringBufferSize = 1024 * 1024;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    @Test
    public void testFirst() {


        /**
         * 1 eventFactory: 消息(OrderEvent)工厂，生产者， 用于生产OrderEvent对象
         * 2 ringBufferSize: 设置容器的长度
         * 3 executor: 自定义线程池，如果生产环境，请强制实现自定义的线程池，设置上界和线程池的RejectedExecutionHandler拒绝策略
         * 4 producerType： 设置生产者类型， 单线程或多线程
         * 5 waitStrategy: 设置等待策略，本例使用的是阻塞的等待策略
         */

        //        1. 实例化 disruptor 对象
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                eventFactory,
                ringBufferSize,
                executor,
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

//        2.添加消费者的监听（配置disruptor与消费者的关联关系）
        disruptor.handleEventsWith(new OrderEventHandler());

//        3.启动disruptor
        disruptor.start();

//        4. 获取disruptor的存数据的容器
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        OrderEventProducer producer = new OrderEventProducer(ringBuffer);

//        5. 准备数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (int i = 0; i < 100; i++) { //产生100个数据
            byteBuffer.putLong(0, i);
            producer.sendDate(byteBuffer);
        }

//        6.使用完需要关闭disruptor和executor线程池
        disruptor.shutdown();
        executor.shutdown();
    }
}
