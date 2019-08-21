package me.togo.demo.disruptor.height.multi;

import com.lmax.disruptor.ExceptionHandler;

public class EventExceptionHandler implements ExceptionHandler<Order> {
    @Override
    public void handleEventException(Throwable ex, long sequence, Order event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
