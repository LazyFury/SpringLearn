package io.lazyfury.utils.response;

import io.lazyfury.utils.error.ErrorCode;

public class JsonResponse<T> {
    public int code;
    public String message;
    public T data;

    public JsonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public JsonResponse(ErrorCode code) {
        this.code = code.code;
        this.message = code.message;
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<>(200, "success", data);
    }

    public static <T> JsonResponse<T> success() {
        return new JsonResponse<>(200, "success");
    }


    public static <T> JsonResponse<T> fail(String message) {
        return new JsonResponse<>(400, message);
    }

    public static <T> JsonResponse<T> fail(int code, String message) {
        return new JsonResponse<>(code, message);
    }

    public static <T> JsonResponse<T> fail(int code, String message, T data) {
        return new JsonResponse<>(code, message, data);
    }

}
