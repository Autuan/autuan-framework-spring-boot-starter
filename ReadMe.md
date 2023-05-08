# 用于各项目的通用底层框架支持

## 使用说明
### 通过 maven 引入
- [ ] 文档未完成,等待施工
### 自行打包
- [ ] 文档未完成,等待施工

- maven 仓库配置地址：
```` xml
<server>
    <id>autuan</id>
    <password>${token}</password>
</server>
````

## 版本
### 当前版本
1.17.0-SNAPSHOT
### 关于版本号说明
maven打包的版本号，中间版本号为支持的JDK版本  
1.17.0 : JDK 17版本
1.8.0 : JDK 8版本

代码中 master 分支为 JDK 17版本， JDK 8版本请切换到 java8 分支

## 功能
### 1.x.0
- [x] Hutool 工具类  
  Hutool 版本： 5.7.17  
  Hutool 文档地址 : [点击跳转](https://hutool.cn/docs/#/)
- [x] Guava 工具类  
  Guava 版本: 31.1-jre  
  Guava 地址 [GitHub地址](https://github.com/google/guava)
- [x] 用于Web响应的通用类 `Result`
  ````java
  // 部分细节略
  public class Result {
    private String code;
    private T data;
    private String msg;
    private String timestamp;
  }
  ````
  
- [x] 运行时业务异常 BusinessException & 部分泛用型枚举
- [x] 验证码生成工具
- [x] 钉钉群聊机器人消息推送
- [x] 企业微信群聊机器人消息推送
- [x] Redis 缓存注解
- [x] 短信发送 工具类
- [x] BCryptPasswordEncoder密码工具类

### 1.x.1
- [ ] 业务异常扩展
- [ ] 对象存储工具 AliYun OSS
- [ ] 对象存储工具 MinIO



## 作者信息
Autuan