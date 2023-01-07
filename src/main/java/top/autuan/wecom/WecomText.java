package top.autuan.wecom;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WecomText {
    private String content;

    private List<String> mentioned_mobile_list;
}
