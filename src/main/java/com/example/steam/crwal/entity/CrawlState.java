package com.example.steam.crwal.entity;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */
public enum CrawlState {
    SUCCESS(1001, "爬取成功"),
    ERROR(1002, "爬取失败");

    private int code;
    private String state;

    CrawlState(int code, String state) {
        this.code = code;
        this.state = state;
    }

    public int getcode() {
        return code;
    }

    public String getState() {
        return state;
    }

    /**
     * 通过code取枚举
     *
     * @param state
     * @return
     */
    public static CrawlState getCodeByState(String state) {
        if (CrawlState.isNull(state)) {
            return null;
        }
        int valueKey = Integer.parseInt(state);
        for (CrawlState enums : CrawlState.values()) {
            if (enums.getcode() == valueKey) {
                return enums;
            }
        }
        return null;
    }

    public static CrawlState getTypeBystate(String state) {
        if (CrawlState.isNull(state)) {
            return null;
        }
        for (CrawlState enums : CrawlState.values()
        ) {
            if (enums.getState().equals(state)) {
                return enums;
            }
        }
        return null;
    }

    private static boolean isNull(String value) {
        if (value == null) {
            return true;
        }
        return false;
    }

    /**
     * 通过code取描述
     *
     * @param value
     * @return
     */
    public static String getDescByValue(int value) {
        for (CrawlState enums : CrawlState.values()) {
            if (enums.getcode() == value) {
                return enums.getState();
            }
        }
        return "";
    }
}
