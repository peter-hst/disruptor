package me.togo.demo.disruptor.height;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TradeTest {

    @Test
    public void trade() throws Exception {
        ExecutorService es1 = Executors.newFixedThreadPool(4);
        ExecutorService es2 = Executors.newFixedThreadPool(4);

//        1.构建
        Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        },
                1024 * 1024,
                es1,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy());


//        2.关联消费者，把消费者设置到disruptor中
//        2.1 串行操作
/*        disruptor
                .handleEventsWith(new Handler1())
                .handleEventsWith(new Handler2())
                .handleEventsWith(new Handler3());*/

//      2.2 并行操作
/*
        disruptor.handleEventsWith(new Handler1());
        disruptor.handleEventsWith(new Handler2());
        disruptor.handleEventsWith(new Handler3());
*/
//      2.3 并行操作
//        disruptor.handleEventsWith(new Handler1(), new Handler2(), new Handler3());

//      2.4 菱形操作, Handler1和Handler2并行操作，Handler3串行操作
/*        disruptor
                .handleEventsWith(new Handler1(), new Handler2())
                .handleEventsWith(new Handler3());*/
//      2.4 菱形操作, Handler1和Handler2并行操作，Handler3串行操作
        disruptor.handleEventsWith(new Handler1(), new Handler2())
                .then(new Handler3());

//        3.启动disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);
        long begin = System.currentTimeMillis();
        es1.submit(new TradePublisher(latch, disruptor));
        latch.await();
        disruptor.shutdown();
        es1.shutdown();
        es2.shutdown();
        System.out.println("消耗了：" + (System.currentTimeMillis() - begin));
    }
}
