package com.zy.demo.projectreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * <p>Title: 实现自定义订阅者</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/12 下午 17:48
 */
@Slf4j
public class Example2 {

    @Test
    public void test1(){
        Subscriber<String> subscriber = new Subscriber<String>() {

            volatile Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                log.info("initial request for 1 element");
                subscription.request(1);
            }

            @Override
            public void onNext(String s) {
                log.info("onNext:{}",s);
                log.info("requesting 1 more element");
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.info("onError:{}",t.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("oncomplete");
            }
        };

        Flux<String> stream = Flux.just("hello","wolrd","!");

        stream.subscribe(subscriber);
    }

    public class MySubscriber<T> extends BaseSubscriber<T>{
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
           log.info("initial request for 1 element");
           request(1);
        }

        @Override
        protected void hookOnNext(T value) {
            log.info("onNext:{}",value);
            log.info("requesting 1 more element");
            request(1);
        }
    }
}
