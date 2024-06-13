package com.itheima.consumer.mq;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Component
public class SpringRabbitListener {
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(Message message){
        System.out.println("spring 消费者接收到消息：【" + message.getMessageProperties().getMessageId() + "】");
        System.out.println("spring 消费者接收到消息：【" + new String(message.getBody()) + "】");
        if (true) {
//            throw new RuntimeException("故意的");
        }
        log.info("消息处理完成");
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("消费者1接收到Fanout消息：【" + msg + "】");
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("消费者2接收到Fanout消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))    public void listenDirectQueue1(String msg) {
        System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue2(String msg) {
        System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String msg){
        System.out.println("消费者1接收到topic.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String msg){
        System.out.println("消费者2接收到topic.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "object.queue")
    public void listenSimpleQueueMessage(Map<String, Object> msg) throws InterruptedException {
        System.out.println("消费者接收到object.queue消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue", durable = "true"),
            exchange = @Exchange(name = "delay.direct", delayed = "true"),
            key = "delay"
    ))
    public void listenDelayMessage(String msg){
        log.info("接收到delay.queue的延迟消息：{}", msg);
    }
}
