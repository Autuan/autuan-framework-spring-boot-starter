# 用于各项目的通用底层框架支持

## 使用说明
maven 仓库配置地址：
```` xml
<server>
    <id>autuan</id>
    <password>${token}</password>
</server>
````
## 当前版本
1.0.0-SNAPSHOT

## 已有功能
* Hutool 工具类  
  Hutool 版本： 5.7.17  
  Hutool 文档地址 : [点击跳转](https://hutool.cn/docs/#/)
* Guava 工具类  
  Guava 版本: 31.1-jre  
  Guava 地址 [GitHub地址](https://github.com/google/guava)
* 用于Web响应的通用类 `Result`
  ````java
  // 部分细节略
  public class Result {
    private String code;
    private T data;
    private String msg;
    private String timestamp;
  }
  ````
  
* 运行时业务异常 BusinessException & 部分泛用型枚举
* 验证码生成工具
* 钉钉群聊机器人消息推送
* 企业微信群聊机器人消息推送
* Redis 缓存注解
* 短信发送 工具类
* BCryptPasswordEncoder密码工具类

## 更新计划：
### 1.0.x
文档补全

### 1.1.0
业务异常扩展


## 作者信息
Autuan