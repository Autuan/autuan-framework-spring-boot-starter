package top.autuan.wecom;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WecomMsgText {

    private String msgtype;

    private WecomText text;
}
