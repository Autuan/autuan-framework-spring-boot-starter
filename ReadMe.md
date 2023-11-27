# 用于各项目的通用底层框架支持

## 使用说明
### 直接引用
#### JDK17 
pom.xml 引入 
```
<dependency>
  <groupId>top.autuan</groupId>
  <artifactId>autuan-framework-spring-boot-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```
最新的版本号可以前往 Release 页面查看

### 自行打包
将此项目下载下来  
```bash
git clone https://github.com/Autuan/autuan-framework-spring-boot-starter.git
```  

使用 maven 安装到本地仓库
```bash
mvn clean install
```

如果你有私服，也可以安装到私服中d
```bash
mvn clean deploy
```

安装在本地或推送到私服后，在项目中引入即可
```` xml
<dependency>
    <groupId>top.autuan</groupId>
    <artifactId>autuan-framework-spring-boot-starter</artifactId>
    <version>${版本号}</version>
</dependency>
````


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
- [x] Hutool 工具类  
  Hutool 版本： 5.7.17  
  Hutool 文档地址 : [点击跳转](https://hutool.cn/docs/#/)
- [x] Guava 工具类  
  Guava 版本: 31.1-jre  
  Guava 地址 [GitHub地址](https://github.com/google/guava)
- [x] 用于Web响应的通用类 `Result`
  ```java
  // 部分细节略
  public class Result {
    private String code;
    private T data;
    private String msg;
    private String timestamp;
  }
  ```
  
- [x] 运行时业务异常 BusinessException & 部分泛用型枚举
- [x] 验证码生成工具
- [x] 钉钉群聊机器人消息推送
- [x] 企业微信群聊机器人消息推送
- [x] Redis 缓存注解
- [x] 短信发送 工具类
- [x] BCryptPasswordEncoder密码工具类

- [ ] 短链
- [ ] 短信发送 阿里云
- [ ] 短信发送 腾讯云
- [ ] 短信发送 极光
- [ ] 业务异常扩展
- [ ] 对象存储工具 AliYun OSS
- [ ] 对象存储工具 MinIO
- [ ] TraceId 生成工具


## 作者信息
Autuan
