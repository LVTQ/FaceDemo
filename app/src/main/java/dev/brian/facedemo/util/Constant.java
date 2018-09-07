package dev.brian.facedemo.util;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/6
 * 描述: 常量类
 */

public class Constant {
    public static final String Key = "kCAwxQdUjpZnq8SMH2C0l20tDLzAYZ6u";
    public static final String Secret= "MPnGPZaFE9LmcWF_Yxhi0BcemXmZzewb";

    //人脸识别
    final public static String URL_DETECT="https://api-cn.faceplusplus.com/facepp/v3/detect";
    //手势识别
    final public static String URL_GESTURE="https://api-cn.faceplusplus.com/humanbodypp/beta/gesture";
    //人脸1:1搜索
    final public static String URL_COMPARE="https://api-cn.faceplusplus.com/facepp/v3/compare";
    //人脸1：N搜索
    final public static String URL_SEARCH="https://api-cn.faceplusplus.com/facepp/v3/search";
    //身份证识别
    final public static String URL_OCRIDCARD="https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
    //驾照识别
    final public static String URL_OCRDRIVERLICENSE="https://api-cn.faceplusplus.com/cardpp/v1/ocrdriverlicense";
    //行驶证识别
    final public static String URL_OCRVEHICLELICENSE="https://api-cn.faceplusplus.com/cardpp/v1/ocrvehiclelicense";
    //人脸辅助api  保存
    final public static String URL_FACESET="https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
}
