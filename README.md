#关于统一缓存工具的使用与扩展说明

### 1.作用：
 系统统一缓存，让开发者不用管缓存的具体实现，更专注于业务逻辑。

### 2.实现功能：
粗粒度的缓存+相对细粒度的缓存。

### 3.如何使用:

    1.注解的介绍:
       
       1.Cached      
          1.用在类上，用于标记需要缓存的类。
          2.属性value用于标示缓存分组，默认值cache
       
       2.Cacheable
          1.用在需要缓存的方法上。
          2.属性cache 标示使用哪一种缓存方式。目前支持redis，memcache。
          3.属性value 用于标示缓存分组，默认值bst。
          4.属性String[]  keys用于标示用那几个字段作为缓存key。可以是参数或参数的属性，
            当有多个重复的属性名时：可以使用[参数名.属性名]的形式(user.name)。
          5.属性survivalTime缓存存活时间，默认30分钟，单位是秒。-1标示永不过期。

       3.CacheEvict 或者 CacheEvicts(表示多个CacheEvict )
          1.用在缓存有更新的方法上。
          2.属性介绍参见 Cacheable
       4.对于部分统一缓存满足不了的需求可使用CacheSupport接口来手动控制。
       5.引入applicationContext-cache.xml文件到需要缓存的工程中。

### 4.如何扩展
       1.实现CacheHandler接口或继承AbstractCacheHandler类
       2.将新的缓存处理器注入到CacheInterceptor 拦截器中
       3.就可以在Cacheable注解上选择相应缓存处理器了。  
       
### 5.Demo

       1.代码示例
       
       @Cached
       public class DemoServiceImpl implements IDemoService {
       
           /**
            * 缓存
            */
           @Cacheable(keys = "id")
           public String query(String id, String name) {
               return "helloworld";
           }
       
           /**
            * 更新
            */
           @CacheEvict(keys = "id")
           public void update(String id, String name) {
           }
       
           /**
            * 查询
            */
           @Cacheable(keys = "id")
           @Override
           public String query1(String id) {
               return null;
           }
       
           /**
            * 更新
            */
           @CacheEvict(keys = "id")//or  @CacheEvict(keys = "demo.id")
           @Override
           public void update1(Demo demo) {
       
           }
       }
       
       
       2.配置文件
       
       <import resource="classpath:cache/applicationContext-cache.xml"></import>
       <bean id="demoService" class="com.msimw.demo.service.DemoServiceImpl"></bean>
              

                                                                                                                                           2017-07-18