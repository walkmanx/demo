package com.zy.demo.projectreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * <p>Title: 用操作符转换响应式序列</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/13 上午 9:18
 */
@Slf4j
public class Example3 {

    /**
     * 1.映射响应式序列元素
     */
    @Test
    public void test1(){
        Flux.range(2018,5)
            // 使用timestamp操作符添加当前时间戳
            .timestamp()
            // 使用index操作符实现枚举
            .index()
            // 订阅序列并记录元素
            .subscribe(e->{
                log.info("index:{},ts:{},value:{}",
                        e.getT1(),
                        Instant.ofEpochMilli(e.getT2().getT1()),
                        e.getT2().getT2());
            });
    }

    /**
     * 2.过滤响应式序列
     */
    @Test
    public void test2(){
        Flux.just(1,6,2,8,3,1,5,1)
                .filter(e-> e % 2 == 0)
                .subscribe(System.out::println);
    }

    /**
     * 3.收集响应式序列
     */
    @Test
    public void test3(){
        Flux.just(1,6,2,8,3,1,5,1)
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    /**
     * 4.裁剪流中元素
     */
    @Test
    public void test4(){
        Flux.just(3,5,7,9,11,15,16,17)
                .any(e-> e % 2 == 0)
                .subscribe(hasEvens->log.info("Has Evens : {}",hasEvens));
        Flux.range(1,5)
                .reduce(0,(acc,elem) -> acc + elem)
                .subscribe(result -> log.info("Reduce Result : {}",result));

        Flux.range(1,5)
                .scan(0,(acc,elem) -> acc + elem)
                .subscribe(result -> log.info("Scan Result : {}",result));

        int bucketSize = 5;
        Flux.range(1,500)
                .index()
                .scan(new int[bucketSize],(acc,elem)-> {
                    acc[(int) (elem.getT1() % bucketSize)] = elem.getT2();
                    return acc;
                })
                .skip(bucketSize)
                .map(array -> Arrays.stream(array).sum() * 1.0 / bucketSize)
                .subscribe(av -> log.info("Runnin average: {}",av));

        Flux.just(1,2,3)
                .thenMany(Flux.just(4,5))
                .subscribe(e -> log.info("onNext: {}",e));
    }

    /**
     * 5.组合响应式流（concat、merge、zip、combineLatest）
     */
    @Test
    public void test5(){
        Flux.concat(
                Flux.range(1,3),
                Flux.range(4,2),
                Flux.range(6,5)
        ).subscribe(e -> log.info("Concat onNext:{}",e));

        Flux.merge(
                Flux.range(1,3),
                Flux.range(4,2)
        ).subscribe(e -> log.info("Merge onNext:{}",e));
    }

    /**
     * 6.流元素批处理(buffering、windowing、grouping)
     */
    @Test
    public void test6(){
        Flux.range(1,13)
                .buffer(4)
                .subscribe(e -> log.info("onNext:{}",e));

        Flux<Flux<Integer>> windowedFLux = Flux.range(101,20)
                .windowUntil(this::isPrime,true);

        windowedFLux.subscribe(windiw -> windiw.collectList().subscribe(
                e -> log.info("window: {}",e)
        ));
    }

    /**
     * 判断一个数是否是素数
     * @param a
     * @return
     */
    public boolean isPrime(int a){
        if (a < 2) {
            return false;
        }
        boolean ean = true;

        for(int i = 2 ; i <= Math.sqrt(a) ; i++){ //Math.sqrt 是调用Math类中的sqrt方法，求一个数的平方根
            if(a%i == 0){
                ean = false;
                break;
            }
        }
        return ean;
    }

    /**
     * 7.flatMap、contatMap、flatMapSequential操作符
     */
    @Test
    public void test7(){
        Flux.just("user1","user2","user3")
                .flatMap(u -> requestBooks(u))
                .map(b -> 1 + "/" + b)
                .subscribe(e -> log.info("onNext:{}",e));
    }

    public Flux<String> requestBooks(String user){
        return Flux.range(1,new Random().nextInt(3) + 1)
                .map(i -> "book" + i)
                .delayElements(Duration.ofMillis(3));
    }

    /**
     * 8.元素采样
     */
    @Test
    public void test8(){
        Flux.range(1,100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20))
                .subscribe(e -> log.info("onNext:{}",e));

    }

    /**
     * 9.将响应式序列转化为阻塞结构
     */
    @Test
    public void test9(){

    }

    /**
     * 10.在序列处理时查看元素
     */
    @Test
    public void test10(){

        Flux.just(1,2,3)
                .concatWith(Flux.error(new RuntimeException("conn error")))
                .doOnEach(s -> log.info("signal:{}",s))
                .subscribe();

    }

    /**
     * 11.物化和非物化信号
     */
    @Test
    public void test11(){
        Flux.range(1,3)
                .doOnNext(e -> log.info("data:{}",e))
                .materialize()
                .doOnNext(s -> log.info("signal:{}",s))
                .dematerialize()
                .collectList()
                .subscribe(result -> log.info("result:{}",result));
    }
}