package me.togo.demo.disruptor.quickstart;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务中的订单对象，Event相当于消息的概念
 */
@Data
public class OrderEvent implements Serializable {
    private long value;     //订单的价格
}
