package top.autuan.sms;

import top.autuan.web.exception.BusinessException;

public interface SmsComponent {
    void sendCode(String mobile);

    Boolean verify(String mobile, String userInput);

    default void send(String mobile, String content){
        throw new BusinessException("NOT SUPPORT");
    }
}
