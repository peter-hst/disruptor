package me.togo.demo.disruptor.height;

import com.lmax.disruptor.EventHandler;

public class Handler4 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Handler 4 : RESET PRICE");
        event.setPrice(17.0);
    }
}
