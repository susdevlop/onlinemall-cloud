onlinemall 的 springcloud 版本
- 开发环境
```
jdk17
mysql8.0.24 
springcloud 2024.0.1
springboot 3.4.4
mybatis-plus 3.5.7
sa-token 1.42.00
dubbo 3.3.4
springcloud.nacos 2023.0.3.2
nacos 2.5.1
redis  7.4.2
rocketmq 5.1.3
react 18
unocss 0.61.0
vite 6.0.5
antd 5.18.1
```
### 各服务信息，8xxx 为部署的端口号,各服务通过--server.port=8xxx指定运行

- 网关服务omc-gateway-810x:  
　负责所有请求的转发，sa-token 负载均衡等功能。  
　

- 用户服务omc-user-820x:  
　用户管理

- 商品服务omc-goods-830x:  
　商品管理、库存管理  


- 订单服务omc-order-840x:  
　负责订单生成等


- 队列服务omc-mq-850x:  
　rocketmq做秒杀订单队列和未付款恢复订单状态,启动时将会自动初始化生成 topic  


- 基础通用模块omc-common:  
　config 配置和各种工具类和到处借鉴来但未使用的工具类

- 前端文件 om-frontend:  
　react18 + vite写的极其简陋界面




note：


- 使用Feign实现调用其他服务时，和 dubbo 的区别是 dubbo 的接口需要被 server 实现。而 feign不需要被实现，
  而是通过注解去定义被调用方的 name 和 path，然后通过 name 和 path 去匹配服务

- Spring Cloud 2020.x 及以后版本（如 Spring Cloud 2023.x）不需要显式添加 @EnableDiscoveryClient，只要引入了 spring-cloud-starter-alibaba-nacos-discovery 依赖，
  服务会自动注册到 Nacos Spring Cloud 2020 之前版：需要手动添加 @EnableDiscoveryClient。

- nacos 后台新建配置的时候，dataid 填写omc-gateway-810x，那么本地的配置文件就要这样写  spring.config.import: nacos:omc-gateway-810x

- 除gateway 和 mq 服务，其他用通用配置 common-config
- 使用 omline-mall中原有的限流桶，理论可以使用springcloud gateway 的Resilience4j 来实现
- 只有队列服务使用 dubbo，其他使用 feign