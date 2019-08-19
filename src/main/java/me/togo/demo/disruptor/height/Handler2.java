package me.togo.demo.disruptor.height;

import com.lmax.disruptor.EventHandler;

import java.util.UUID;

public class Handler2 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Handler 2 : SET ID");
        Thread.sleep(2000);
        event.setId(UUID.randomUUID().toString());

    }
}
