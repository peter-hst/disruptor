package me.togo.demo.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 生产数据的对象
 */
public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendDate(ByteBuffer byteBuffer) {
//        1. 生产者发布数据的时候，首先从ringBuffer 中获取sequence序号；
        long sequence = ringBuffer.next();
        try {
            //        2.根据这个sequence序号去找到OrderEvent对象，注意：此时的OrderEvent是一个没有赋值的空对象
            OrderEvent orderEvent = ringBuffer.get(sequence);

            //        3.给OrderEvent对象填充值
            orderEvent.setValue(byteBuffer.getLong(0));
        } finally {
            //        4.生产者提交发布数据
            ringBuffer.publish(sequence); //官方建议 把publish写到finally{}里
        }

    }
}
