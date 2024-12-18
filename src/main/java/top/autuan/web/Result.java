package top.autuan.web;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.autuan.web.enums.BaseEnum;
import top.autuan.web.enums.ResultEnumInterface;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private String code;
    private T data;
    private String msg;
    private String timestamp;

    public static <T> Result<T> check(boolean bool, String failMsg) {
        return bool ? ok() : fail(failMsg);
    }

    public static <T> Result<T> check(boolean bool, ResultEnumInterface enumInterface) {
        return bool ? ok() : fail(enumInterface);
    }


    public static <T> Result<T> response(T data) {
        return ok(data);
    }

    public static <T> Result<T> ok(T data) {
        return ok(BaseEnum.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }
    public static <T> Result<T> ok(String code, T data) {
        return response(code, data, BaseEnum.SUCCESS.getMessage());
    }

    public static <T> Result<T> fail(String message) {
        return fail(BaseEnum.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(String code, String message) {
        return response(code, null, message);
    }

    public static <T> Result<T> fail(ResultEnumInterface enumInterface) {
        return fail(enumInterface.getCode(), enumInterface.getMessage());
    }

    public static <T> Result<T> response(String code, T data, String message) {
        return response(code, data, message, LocalDateTime.now());
    }

    public static <T> Result<T> response(String code, T data, String message, LocalDateTime timestamp) {
        Result<T> result = new Result<>();
        result.setTimestamp(LocalDateTimeUtil.formatNormal(timestamp));
        result.setCode(code);
        result.setData(data);
        result.setMsg(message);
        return result;
    }
}
