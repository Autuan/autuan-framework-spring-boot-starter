package top.autuan.sms;

public class AliYunSmsImpl implements SmsComponent{
    @Override
    public void sendCode(String mobile) {
        // todo impl
    }

    @Override
    public Boolean verify(String mobile, String userInput) {
        // todo impl
        return null;
    }

    @Override
    public void send(String mobile, String content) {
        // todo impl
//        SmsComponent.super.send(mobile, content);
    }
}
