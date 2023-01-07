package top.autuan.wecom;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import top.autuan.dingTalk.ProjectProps;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WecomComponent {
    private final String URL;
    private final String ENV;
    private final String PROJECT_NAME;

    public WecomComponent(WecomProps wecomProps, ProjectProps projectProps) {
        this.ENV = projectProps.getEnv();
        this.PROJECT_NAME = projectProps.getName();
        this.URL = wecomProps.getUrl();
    }

    /**
     * 钉钉群项目预警异常
     *
     * @param e 出现的异常
     */
    public void sendError(Exception e) {
        StackTraceElement trace = null;
        StackTraceElement[] traceElements = e.getStackTrace();
        if (traceElements != null && traceElements.length > 0) {
            trace = traceElements[0];
        }
        String message = e.getMessage();
        String content = StrUtil.format("业务异常报警 : \n 环境： {} \n  项目： {} \n {} \n  trace:{} \n 烦请各位尽快处理\n",
                ENV, PROJECT_NAME, message, trace);

        send(content);
    }

    /**
     * 钉钉群项目消息
     *
     * @param msg 要发送的内容
     */
    public void sendMsg(String msg) {
        String content = StrUtil.format("message : \n 环境： {} \n  项目： {} \n {} \n", ENV, PROJECT_NAME, msg);
        send(content);
    }

    private void send(String content) {
        boolean isSend = isSend();

        if (isSend) {
            boolean isAtAll = isAtAll();
            List<String> mentionedMobileList = new ArrayList<>();
            if (isAtAll) {
                mentionedMobileList.add("@all");
            }
            WecomText text = WecomText.builder()
                    .content(content)
                    .mentioned_mobile_list(mentionedMobileList)
                    .build();
            WecomMsgText msg = WecomMsgText.builder()
                    .msgtype("text")
                    .text(text)
                    .build();
            log.debug("DingTalkComponent -> send -> url -> {}", URL);
            log.debug("DingTalkComponent -> send -> body -> {}", JSONUtil.toJsonStr(msg));
            String responseStr = HttpUtil.post(URL, JSONUtil.toJsonStr(msg));
            log.debug("DingTalkComponent -> send -> response -> {}", responseStr);

        }
    }

    private boolean isAtAll() {
        // 只有 生产环境 atAll
        return "prod".equals(ENV);
    }

    private boolean isSend() {
        // 只有 uat 和 prod 发送钉钉预警
        return "prod".equals(ENV) || "uat".equals(ENV);
    }

}
