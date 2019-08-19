package me.togo.demo.disruptor.height;

import com.lmax.disruptor.EventHandler;

public class Handler3 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Handler 3 : ID:" + event.getId() + ", NAME:" + event.getName() + ", PRICE:" + event.getPrice() + ", INSTANCE:" + event.toString());
    }
}
