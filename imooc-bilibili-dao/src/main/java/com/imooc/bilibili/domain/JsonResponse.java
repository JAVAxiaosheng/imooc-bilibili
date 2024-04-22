package com.imooc.bilibili.domain;

import lombok.Data;

@Data
public class JsonResponse<T> {
    private String code;
    private String msg;
    private T data;

    public JsonResponse() {
    }

    public JsonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data) {
        this.data = data;
        msg = "success";
        code = "0";
    }

    public static JsonResponse<String> success() {
        return new JsonResponse<String>(null);
    }

    public static JsonResponse<String> success(String data) {
        return new JsonResponse<String>(data);
    }

    public static JsonResponse<String> fail() {
        return new JsonResponse<String>("1", "fail");
    }

    public static JsonResponse<String> fail(String code, String msg) {
        return new JsonResponse<String>(code, msg);
    }

}
