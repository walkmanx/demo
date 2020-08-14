package com.zy.demo.projectreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>Title: 创建FLux序列和Mono序列，订阅响应式流</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/12 下午 17:26
 */
@Slf4j
public class Example1 {

    @Test
    public void test1(){
        Flux<String> stream1 = Flux.just("hello","world");
        Flux<Integer> stream2 = Flux.fromArray(new Integer[]{1,2,3});
        Flux<Integer> stream3 = Flux.fromIterable(Arrays.asList(9,8,7));
        Flux<Integer> stream4 = Flux.range(2010,10);

        Mono<String> stream5 = Mono.just("one");
        Mono<String> stream6 = Mono.justOrEmpty(null);
        Mono<String> stream7 = Mono.justOrEmpty(Optional.empty());

        Mono<String> stream8 = Mono.fromCallable(() -> httpRequest());

        stream1.subscribe(System.out::println);

        stream1.subscribe(data-> log.info("onNext:{}",data),err->{

        },()->{
            log.info("onComplete");
        });

        Flux.range(1,100).subscribe(data->{
            log.info("onNext:{}",data);
        },err ->{

        },()->{
            log.info("onComplete");
        },subscription -> {
            subscription.request(4);
            subscription.cancel();
        });
    }

    private static String httpRequest(){
        return "这是一个http请求";
    }

}
