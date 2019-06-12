package com.imooc.enums;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
public enum VideoStatusEnum {
    SUCCESS(1),		// 发布成功
    FORBID(2);		// 禁止播放，管理员操作

    public final int value;

    VideoStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
