package me.togo.demo.disruptor.height.single;

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
    public void tradeSingle() throws Exception {
        ExecutorService es1 = Executors.newFixedThreadPool(5);
//        用于提交任务的线程池
        ExecutorService es2 = Executors.newFixedThreadPool(1);

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
/*        disruptor.handleEventsWith(new Handler1(), new Handler2())
                .then(new Handler3());*/
//        2.5 六边形操作 ，注意：单线程下 disruptor线程数量需要和handler的数量一致，否则结果是错误的
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();
        disruptor.handleEventsWith(h1, h4);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2, h5).handleEventsWith(h3);
//        3.启动disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);
        long begin = System.currentTimeMillis();
        es2.submit(new TradePublisher(latch, disruptor));
        latch.await();
        disruptor.shutdown();
        es1.shutdown();
        es2.shutdown();
        System.out.println("消耗了：" + (System.currentTimeMillis() - begin));
    }
}
