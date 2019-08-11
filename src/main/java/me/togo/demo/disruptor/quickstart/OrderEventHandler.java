package me.togo.demo.disruptor.quickstart;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * handler是个消费者，用于消费OrderEvent
 */

@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("消费者 Order value: {}", event.getValue());
    }
}
