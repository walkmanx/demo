package com.zy.demo.component;

import com.zy.demo.domain.Temperature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: 温度传感器组件</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/12 下午 14:51
 */
@Component
public class TemperatureSensor {

    private final ApplicationEventPublisher publisher;
    private final Random rnd = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    private void startProcessing(){
        this.executor.schedule(this::probe,1, TimeUnit.SECONDS);
    }

    private void probe(){
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));
        executor.schedule(this::probe,rnd.nextInt(5000),TimeUnit.MILLISECONDS);
    }

}
