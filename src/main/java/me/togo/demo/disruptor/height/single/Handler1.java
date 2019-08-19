package me.togo.demo.disruptor.height.single;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {

//    实现了EventHandler接口
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

//    实现了WorkHandler接口
    @Override
    public void onEvent(Trade event) throws Exception {
        System.out.println("Handler 1 : SET NAME");
        Thread.sleep(1000);
        event.setName("H1");

    }
}
