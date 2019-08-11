package me.togo.demo.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * 用于生产OrderEvent的实例，生产者
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
