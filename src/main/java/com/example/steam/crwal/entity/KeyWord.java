package com.example.steam.crwal.entity;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */
public enum KeyWord {
    STEAM(001, "STEAM教育"),
    STEAM2(002, "STEAM教育行业市场分析报告"),
    STEAM3(002, "近几年STEAM教育行业市场投资调研分析报告");
    private int key;
    private String value;

    KeyWord(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(KeyWord keyWord) {
        for (KeyWord k : KeyWord.values()){
            if (keyWord.equals(k)){
                return k.value;
            }
        }
        return null;
    }
}
