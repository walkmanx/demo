package com.zy.demo.domain;

import lombok.Data;

/**
 * <p>Title: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Version:zhuoyuan V2.0</p>
 *
 * @author gc
 * @description
 * @date 2020/8/12 下午 14:50
 */
@Data
public class Temperature {
    // 温度值
    private Double value;

    public Temperature(double value){
        this.value = value;
    }
}
