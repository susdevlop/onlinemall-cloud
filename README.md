onlinemall 的 springcloud 版本
note：


- 使用Feign实现调用其他服务时，和 dubbo 的区别是 dubbo 的接口需要被 server 实现。而 feign不需要被实现，
  而是通过注解去定义被调用方的 name 和 path，然后通过 name 和 path 去匹配服务

- Spring Cloud 2020.x 及以后版本（如 Spring Cloud 2023.x）不需要显式添加 @EnableDiscoveryClient，只要引入了 spring-cloud-starter-alibaba-nacos-discovery 依赖，
  服务会自动注册到 Nacos Spring Cloud 2020 之前版：需要手动添加 @EnableDiscoveryClient。

- nacos 后台新建配置的时候，dataid 填写omc-gateway-810x，那么本地的配置文件就要这样写  spring.config.import: nacos:omc-gateway-810x

- 除gateway 和 mq 服务，其他用通用配置 common-config