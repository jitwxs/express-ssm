## 快递代拿系统 SSM 版

>基于 SpringBoot 全新开发的快递代拿系统已开发完毕，相关技术栈全面升级，[前往查看](<https://github.com/jitwxs/express>)。
>

### 使用技术

采用 Spring + SpringMVC + MyBatisPlus，连接池采用 Druid，安全框架使用 Shiro，前端采用 Bootstrap + layer 实现。

支付采用支付宝沙箱环境，支付APP下载链接，[点击这里](https://sandbox.alipaydev.com/user/downloadApp.htm)。

支付账号：uceskd4358@sandbox.com
登录密码、支付密码：111111

**注意：**

请务必使用以上链接下载`沙箱支付宝`，也务必使用以上账号登录。不要使用真实支付宝APP和真实支付宝账号登录。

### 运行环境

- 集成开发环境：IntelliJ IDEA
- 项目构建工具：Maven
- 数据库：MYSQL 5.7+
- JDK版本：1.8
- Tomcat版本：Tomcat8

（1）首先请创建数据库：

```shell
CREATE DATABASE IF NOT EXISTS `express-ssm` /*!40100 DEFAULT CHARACTER SET utf8 */
```

（2）导入项目 sql 文件夹下的 `express-ssm.sql` 文件。

（3）编辑项目中 `src/main/resources/cnf/mysql.properties` 文件，修改数据库连接信息：

```application
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/express-ssm?useUnicode=true&useSSL=false&characterEncoding=utf-8
jdbc.username=root # MYSQL 用户名
jdbc.password=root # MYSQL 密码
```

> 关于**如何使用 IDEA 运行 SSM 项目**，可以参看[这里](<https://github.com/jitwxs/cloud-note/blob/master/README.md>)，此处不再赘述。

### 默认账户

注：以下为本项目默认存在的用户名密码，请将本仓库项目在本地运行后使用以下密码登录。

| 权限   | 用户名 | 密码 |
| ------ | ------ | :--- |
| 管理员 | admin  | 123  |
| 配送员 | 李四   | 123  |
| 用户名 | 小红   | 123  |

### 截图

![](./screenshot/index.png)

![](./screenshot/alipay.png)

![](./screenshot/order.png)

![](./screenshot/person.png)
