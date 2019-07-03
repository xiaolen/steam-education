package com.example.steam.crwal.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by xl on 2016/6/30.
 */
@Slf4j
public class JsonUtils {

    /**
     * json转化为bean
     * @param json
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String json,Class<T> beanClass){
        try{
            return parseObject(json, beanClass);
        }catch (Exception ex){
            log.error("解析失败", ex);
            return null;
        }
    }
    public static String  objectToJson(){
        return null;
    }

    /**
     * bean转化为json
     * @param bean
     * @return
     * @throws Exception
     */
    public static String beanToJson(Object bean) {
        try{
            return JSONObject.toJSONString(bean);
        }catch (Exception ex){
            log.error("bean转化为json失败", ex);
        }
        return "";
    }

    /**
     * bean转化为json
     * @param bean
     * @return
     * @throws Exception
     */
    public static String objectToJson(Object bean) {
        try{
            return net.sf.json.JSONObject.fromObject(bean).toString();
        }catch (Exception ex){
            log.error("bean转化为json失败", ex);
        }
        return "";
    }

    /**
     * @desc: 获取Json
     * @param: page
     * @author: YuYangjun
     * @date: 2017/12/6 下午3:10
     */
    public static JSONObject getJSONObject(String json) {
        JSONObject jsonObject = null;
        try {
            if (StringUtils.isNotEmpty(json) && isJson(json)) {
                jsonObject = parseObject(json);
            }
        } catch (Exception e) {
            log.error("解析失败", e);
        }
        return jsonObject;
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String json) {
        try {
            JSON.parse(json);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * @desc: 获取Json
     * @param: page
     * @author: YuYangjun
     * @date: 2017/12/6 下午3:10
     */
    public static JSONArray getJSONArray(String json) {
        JSONArray jsonArray = null;
        try {
            if (StringUtils.isNotEmpty(json) && isJson(json)) {
                jsonArray = parseArray(json);
            }
        } catch (Exception e) {
            log.error("解析失败", e);
        }
        return jsonArray;
    }

    /**
     * 获取json下对应key的值
     *
     * @param json 解析的json 是jsonObject
     * @param keys jsonObject只能是全是jsonobject
     * @return 字符串
     */
    public static String parseJson(String json, String... keys) {
        JSONObject data = parseObject(json);
        if (null == data) {
            return null;
        }
        for (int i = 0; i < keys.length; i++) {
            if (null == data) {
                return null;
            }
            if (i == keys.length-1) {
                return data.getString(keys[i]);
            }
            data = data.getJSONObject(keys[i]);
        }
        return null;
    }
}