package me.togo.demo.disruptor.height.single;

import com.lmax.disruptor.EventHandler;

public class Handler5 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Handler 5 : RESET PRICE AGAIN");
        event.setPrice(event.getPrice() + 3.0);
    }
}
