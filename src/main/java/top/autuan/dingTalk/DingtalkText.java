package top.autuan.dingTalk;

import lombok.Builder;
import lombok.Data;

/**
 * @author Autuan.Yu
 */
@Data
@Builder
class DingtalkText {
    private String content;

    public DingtalkText(String content) {
        this.content = content;
    }

    public DingtalkText() {

    }

}
