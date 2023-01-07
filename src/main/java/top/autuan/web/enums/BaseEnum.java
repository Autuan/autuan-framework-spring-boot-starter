package top.autuan.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseEnum implements ResultEnumInterface{
    /**请求成功*/
    SUCCESS("200", "ok"),
    /**请求失败*/
    FAIL("500","fail"),

    CONTENT_TYPE_ERROR("500001","请求体格式不正确"),
    SERVER_UPGRADE("500002","服务器维护中，请稍后重试"),
    NOT_SUPPORT("500003","不支持的请求方式"),
    JSON_XML_FORMAT_ERROR("500004", "JSON/XML解析错误"),

    UN_ROOT_ERROR("500005", "无权限访问"),
    IMG_CODE("500006", "图片验证码错误"),
    SMS_CODE("500007", "短信验证码错误"),
    SMS_SEND("500008", "您已获取过验证码,验证码4小时内有效"),
    NULL("500009", "必填参数不能为空"),
    SIZE("500010", "图片大小限制在5MB以内"),
    UPGRADE("500011", "版本有更新,请关闭后重新进入"),
    FREQUENCY("500012", "请求过于频繁"),
    ;
    private String code;
    private String message;
}
