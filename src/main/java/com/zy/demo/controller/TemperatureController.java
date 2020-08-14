package com.zy.demo.controller;

import com.sun.glass.ui.Size;
import com.zy.demo.domain.Temperature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>Title: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/12 下午 15:05
 */
@RestController
@Slf4j
public class TemperatureController {

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping("/temperature-stream")
    public SseEmitter events(HttpServletRequest request){

        SseEmitter emitter = new SseEmitter();
        clients.add(emitter);

        // 在错误或断开连接时从客户端删除发射器
        emitter.onTimeout(()->{
            log.info("======== 连接超时 =======");
            clients.remove(emitter);
        });

        emitter.onCompletion(()->{
            log.info("======== 连接完成 =======");
            clients.remove(emitter);
        });
        return emitter;
    }

    @Async
    @EventListener
    public void handleMessage(Temperature temperature){
        log.info("========================= 收到传感器数据 ========================== ： " + temperature );

        log.info("========================= 当前客户端数量 ========================== ： " + clients.size());

        List<SseEmitter> deadEmitters = new ArrayList<>();
        clients.forEach(emitter->{
            try {
                log.info("========================= 向客户端发送数据 ========================== ： " + emitter);
                emitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        clients.removeAll(deadEmitters);
    }

}
