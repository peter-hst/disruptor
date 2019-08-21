package me.togo.demo.disruptor.height.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements WorkHandler<Order> {
    private String consumerId;
    private Random random = new Random();

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onEvent(Order event) throws Exception {
        Thread.sleep(1 * random.nextInt(5));
        System.out.println("消费者：" + consumerId + ", 消费信息：" + event.getId());
        count.incrementAndGet();
    }

    public static int getCount() {
        return count.get();
    }
}
