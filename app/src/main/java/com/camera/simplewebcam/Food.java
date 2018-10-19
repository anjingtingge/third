package com.camera.simplewebcam;

/**
 * @author 侯典杰
 * @package org.tensorflow.foodclassify
 * @description: 描述:
 * @date 2018/5/1620:21
 */
/**
 * 食物类
 */
class Food{
    private String foodName;
    private String x1;
    private String y1;
    private String x2;
    private String y2;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getY1() {
        return y1;
    }

    public void setY1(String y1) {
        this.y1 = y1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getY2() {
        return y2;
    }

    public void setY2(String y2) {
        this.y2 = y2;
    }
}

