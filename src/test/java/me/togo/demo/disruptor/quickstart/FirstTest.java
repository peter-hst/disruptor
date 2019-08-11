package me.togo.demo.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import me.togo.demo.disruptor.Const;
import me.togo.demo.disruptor.Data;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static me.togo.demo.disruptor.Const.NUM_OHM;

public class FirstTest {

    @Test
    public void testBookingQueueBySingleThread() {
        final ArrayBlockingQueue<Data> queue = new ArrayBlockingQueue<>(NUM_OHM);
        final CountDownLatch countDownLatch = new CountDownLatch(NUM_OHM);
        long startTime = System.currentTimeMillis();
        new Thread(() -> {
            long i = 0;
            while (i < NUM_OHM) {
                try {
                    queue.put(new Data(i, "ccc_" + i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();

        new Thread(() -> {
            long k = 0;
            while (k < NUM_OHM) {
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                k++;
                countDownLatch.countDown();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("BlockingQueue build " + NUM_OHM + " data set of cost total time:" + (endTime - startTime) + "ms.");
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDisruptorBySingleThread() {
        final int rinngBufferSize = 65536;
        final Random random = new Random(31);
        Disruptor<Data> disruptor = new Disruptor<Data>(new EventFactory<Data>() {
            @Override
            public Data newInstance() {
                return new Data(random.nextLong(), "ccc_" + random.nextLong());
            }
        },
                rinngBufferSize,
                Executors.newSingleThreadScheduledExecutor(),
                ProducerType.SINGLE, new YieldingWaitStrategy());


    }
}
