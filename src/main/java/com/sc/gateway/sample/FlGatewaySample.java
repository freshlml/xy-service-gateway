package com.sc.gateway.sample;


import java.util.function.Predicate;

/**
 * Spring Ecosystem Gateway, including Spring 5, SpringBoot 2, SpringFramework spring-webflux, Reactor; also can concerns security, monitoring/metrics, and resiliency
 *
 * Gateway运行在Spring Boot的Spring WebFlux的Netty中(不支持在servlet容器中运行)，不兼容SpringData, SpringSecurity, SpringFramework spring-webmvc
 *
 * Route: 路由: id, uri, predicate collection, and filter collection定义; 当predicates聚合起来(聚合方式如全部and)返回true,则匹配路由; route配置对应的类{@link org.springframework.cloud.gateway.route.RouteDefinition},RouteDefinition通过{@link org.springframework.cloud.gateway.route.RouteDefinitionLocator}创建
 * Predicate: 谓词逻辑: boolean predicate(...) {...}, java8中lambda表达式提供predicate{@link Predicate}, 输入类型为
 *            ServerWebExchange{@link org.springframework.web.server.ServerWebExchange} which Provides access to the HTTP request and response,如访问Request中的头和参数
 * Filter: 网关拦截器, GatewayFilter实例{@link org.springframework.cloud.gateway.filter.GatewayFilter}, 在发送下游请求之前或之后修改请求和响应
 *
 * Client ---> Gateway Handler Mapping ---> Gateway web Handler ---> target service
 *
 * 第一: Route配置
 * spring:
 *   cloud:
 *     gateway:
 *       routes:
 *         - id: route-service-sample ##如上述Route所说, 配置id
 *           uri: lb://service-user   ##如上述Route所说, 配置uri
 *           predicates:              ##如上述Route所说, 配置predicate集合
 *             - Path=                                      ## 内置的 Path predicate器, 接受两个参数,1.Spring PathMatcher patterns的列表2.matchOptionalTrailingSeparator的可选标志
 *                 /sampleScan/**,                          #   PathMatcher采用Ant风格的路径写法@see {Ant风格的路径}
 *                 /user/**,
 *                 /mall/{placeholder}                      #   placeholder从ServerWebExchange的Attributes取值，{@link org.springframework.web.server.ServerWebExchange }.putUriTemplateVariables
 *             - Method=                                    ## 内置的 Method predicate器, 接受一个String参数，","分隔的method name，return true如果request method满足配置的请求类型
 *                 GET,POST
 *             - Weight=                                    ## 内置的 Weight predicate器, groupName, 占比，其他相同的groupName配置组成一个组，如此处的groupName1组中，他占的权重=8
 *                 groupName1,8
 *             - Query=                                     ## 内置的 Query predicate器, 两个参数,String类型的查询参数名称,可选的regexp表示参数值，return true当请求中有该参数或者该参数的值匹配时
 *                 id,regexp
 *             - RemoteAddr=                                ## 内置的 RemoteAddr predicate器, 一个参数，","分隔的列表, ip/子网掩码，return true当请求的远程地址匹配时
 *                 192.168.1.1/24,192.168.0.1/16
 *             - Cookie=                                    ## 内置的 Cookie predicate器, 接受两个参数,1.String类型表示cookie的名称,2.Regexp表示一个正则匹配，return true当给定的cookie名称不为空并且取出来的值匹配正则时
 *                 cookieKey,cookieValue
 *             - After=                                     ## 内置的 After predicate器, 接受一个ZonedDateTime参数，return true当请求时间大于该参数时
 *                 2017-01-20T17:42:47.789-07:00[America/Denver]
 *             - Before=                                    ## 内置的 Before predicate器, 接受一个ZonedDateTime参数，return true当请求时间小于该参数时
 *                 2017-01-20T17:42:47.789-07:00[America/Denver]
 *             - Between=                                   ## 内置的 Between predicate器, 接受两个ZonedDateTime参数，return true当请求时间介于两者之间时
 *                 2017-01-20T17:42:47.789-07:00[America/Denver],2017-01-21T17:42:47.789-07:00[America/Denver]
 *             - Header=                                    ## 内置的 Header predicate器, 接受两个参数,1.String类型表示header的名称,2.Regexp表示一个正则匹配，return true当请求header存在并且其值匹配正则时
 *                 X-Request-Id, \d+
 *             - Host=                                      ## 内置的 Host predicate器, 接受一个String参数，","分隔的Host列表，每一个Host使用Ant风格路径以"."分隔，return true当请求头中Host的值满足任意一个Host时
 *                 {pro}.spring.cc,**.google.org
 *           filters:    ##如上述Route所说, 配置filter集合
 *             - AddRequestHeader=                          ## 内置的 AddRequestHeader filter器，两个参数，写入request header
 *                 ORG_ID,123
 *             - AddRequestParameter=                       ## 内置的 AddRequestParameter filter器，两个参数，写入查询参数(GET?)
 *                 userId,123
 *             - AddResponseHeader=                         ## 内置的 AddResponseHeader filter器，两个参数，写入response header
 *                 RESOURCE_CODE,position
 *             - DedupeResponseHeader=                      ## 内置的 DedupeResponseHeader filter器，第一个参数:空格分隔的header名称,第二个参数:strategy(可取值=RETAIN_FIRST[默认],RETAIN_LAST,RETAIN_UNIQUE)，网关和下游服务出现重复的response头时根据strategy清除
 *                 Access-Control-Allow-Credentials Access-Control-Allow-Origin
 *             - PrefixPath=                                ## 内置的 PrefixPath filter器，对匹配的路径增加前缀，如/hello, 转发给下游将是/prefix/hello
 *                 /prefix
 *             - RemoveRequestHeader=                       ## 内置的 RemoveRequestHeader filter器，移除指定header,然后转发到下游
 *                 X-Request-Foo
 *             - RemoveResponseHeader=                      ## 内置的 RemoveResponseHeader filter器，移除response中的header，返回给网关客户端
 *                 X-Response-Foo
 *             - name: RequestRateLimiter                   ## 内置的 RequestRateLimiter filter器，用于限流
 *               args:
 *                 rate-limiter: "#{@myRateLimiter}"     #配置{@link org.springframework.cloud.gateway.filter.ratelimit.RateLimiter}实现类
 *                 key-resolver: "#{@userKeyResolver}"   #配置{@link org.springframework.cloud.gateway.filter.ratelimit.KeyResolver}实现类,默认实现类是{@link org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver}
 *                 redis-rate-limiter
 *                   replenishRate: 100                  #用户每秒允许多少个请求
 *                   burstCapacity: 100                  #用户每秒最大请求数,将此值设置为零将阻止所有请求
 *             - StripPrefix=1                              ## 内置的StripPrefix filter器，在转发给下游之前，将路径剥离的数量，如/name/bar/foo ===> /bar/foo
 *             - Hystrix=                                   ## 内置的 Hystrix filter器，hystrixCommandName,fallback地址，(需要hystrix依赖)，将其余filter及后续包装在hystrixCommand中执行
 *                 hystrixCommandName,forward:/fallback      #fallback地址既可以是gateway本地Controller,也可以是下游的服务提供的Controller @see {forward到下游服务}
 *             等等内置的filter见文档
 *
 * forward到下游服务
 *           - id: ingredients-fallback
 *           uri: lb://service-user
 *           predicates:
 *             - Path=
 *                 /fallback
 *           filters:
 *             - FallbackHeaders=                           ## FallbackHeaders，将Hystrix执行的异常信息写到header从而将异常信息传送到下游服务
 *                 executionExceptionTypeHeaderName: Test-Header
 *
 * Ant风格的路径
 * ? 匹配任何单字符
 * * 匹配0或者任意数量的字符
 * ** 匹配0或者更多的目录
 * {placeholder:regexp} match the regexp with the placeholder‘name "placeholder"
 *
 * filter执行顺序
 *  当有请求与路由匹配后，web Handler将所有的GlobalFilter和此路由的filter添加到过滤器链中，通过{@link org.springframework.core.Ordered}排序，具有最高优先级的过滤器在前置执行时第一个执行，在后置执行时最后一个执行
 * gateway提供的GlobalFilter都实现了Ordered，提供的GatewayFilter实现没有Ordered(顺序是如何呢？)，自定义Filter可提供Ordered，不提供顺序如何呢？
 *
 * 第二: GlobalFilter, 全局过滤器,配置成bean，如在 {@link org.springframework.cloud.gateway.config.GatewayAutoConfiguration}中配置
 * 1.LoadBalancerClientFilter,当类路径有ribbon相关类时开启{@link org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration}
 *  1).实现逻辑: 在ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR属性的列表中寻找lb://开头的uri配置，使用Spring Cloud LoadBalancerClient进行负载均衡处理，得到ServerInstance
 *             再将ServerInstance中实例ip地址和port替换原来的lb://...地址,未经修改的原始URL会附加到ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR属性中的列表中
 *             过滤器还将查看ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR属性，以查看其是否等于lb，然后应用相同的规则
 *  2).使用ribbon
 *  3).当在LoadBalancer中找不到服务实例时，将返回503.您可以通过设置spring.cloud.gateway.loadbalancer.use404=true来配置网关以返回404
 * @see org.springframework.cloud.gateway.support.ServerWebExchangeUtils
 * 2.ReactiveLoadBalancerClientFilter
 *  1).实现逻辑: 在ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR属性的列表中寻找lb://开头的uri配置，使用Spring Cloud ReactorLoadBalancer进行负载均衡处理，得到ServerInstance
 *             再将ServerInstance中实例ip地址和port替换原来的lb://...地址,未经修改的原始URL会附加到ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR属性中的列表中
 *             过滤器还将查看ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR属性，以查看其是否等于lb，然后应用相同的规则
 *  2).添加org.springframework.cloud:spring-cloud-loadbalancer依赖并且设置spring.cloud.loadbalancer.ribbon.enabled=false
 * and GlobalFilter
 *
 * HttpHeadersFilter: http header过滤器，配置成bean，如在 {@link org.springframework.cloud.gateway.config.GatewayAutoConfiguration}中配置
 *
 * 第四: TODO 网关与后端服务的ssl/tls
 *
 *
 *
 *
 *
 *
 */
public class FlGatewaySample {

}
