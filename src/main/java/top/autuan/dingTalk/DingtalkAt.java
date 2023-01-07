package top.autuan.dingTalk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DingtalkAt {
    private List<String> atMobiles;
    private boolean isAtAll;
}
