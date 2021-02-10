###1. easy-http 是什么
是否还在为如何写http/https请求而烦恼。是否有想过能有一个包能帮你屏蔽掉一切细节，让你能够专心的应付自己的业务逻辑
那么easy-http正是为了帮你解决这些问题而出现的。<br>
你只需要简单的定义一个接口，标注一下请求方式，就能帮你实现网络请求了。<br>

###2. 如何使用

看个简单的例子：<br>

####2.1 get请求

1. 启动类似加@EnableApi(basePackages = {"com.github.cnzhoutao.api"}) 注解，并标明定义的api所在的路径
2. 定义接口，接口上申明需要请求的接口的信息，不需要实现
```java

import com.github.easy_http.annotation.Api;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zt
 * @Date: 2021/2/10 10:30 上午
 */
@Api(url = "", hostName = "www.jianshu.com", https = true)
public interface JianShuApi {

    @GetMapping(value = "/")
    String indexPage();
}
```
3. 调用
```java

package com.github.cnzhoutao;

import com.github.cnzhoutao.api.JianShuApi;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class EasyHttpTestApplicationTests {

    @Resource
    private JianShuApi jianShuApi;

    @Test
    public void testJianshu() {
        String s = jianShuApi.indexPage();
        System.out.println(s);
    }

}
```

4. 返回结果

   ![image-20210210122538090](https://tva1.sinaimg.cn/large/008eGmZEly1gnibpnjsobj31of0u0dpu.jpg)

####2.2 post请求

1. 先来个controller吧,实现一个简单的登录功能

   ```java
   package com.github.cnzhoutao.controller;
   
   import com.github.cnzhoutao.dto.UserDTO;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestBody;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   /**
    * @author zt
    * @Date: 2021/2/10 12:04 下午
    */
   @RestController
   @RequestMapping(value = "/test")
   public class LoginController {
   
       @PostMapping(value = "/login")
       public String login(@RequestBody UserDTO userDTO) {
           System.out.println("接收到登录请求:用户名:" + userDTO.getUserName() + "----密码：" + userDTO.getPwd());
           return "{\"res\":\"登录成功\"}";
   
       }
   }
   ```
   
2. UserDTO

   ```java
   package com.github.cnzhoutao.dto;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   import lombok.experimental.Accessors;
   
   import java.io.Serializable;
   
   /**
    * @author zt
    * @Date: 2021/2/10 12:05 下午
    */
   @Data
   @Accessors(chain = true)
   @AllArgsConstructor
   @NoArgsConstructor
   public class UserDTO implements Serializable {
   
       //用户名
       private String userName;
   
       //密码
       private String pwd;
   
   }
   ```

   

3. api接口定义

   ```java
   package com.github.cnzhoutao.api;
   
   import com.github.cnzhoutao.dto.UserDTO;
   import com.github.easy_http.annotation.Api;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestBody;
   
   /**
    * @author zt
    * @Date: 2021/2/10 12:08 下午
    */
   @Api(url = "/test", hostName = "127.0.0.1:8080", https = false)
   public interface LoginApi {
   
       @PostMapping(value = "/login")
       String login(@RequestBody UserDTO userDTO);
   
   }
   ```

   

4. 单元测试

   ```java
   package com.github.cnzhoutao;
   
   import com.github.cnzhoutao.api.JianShuApi;
   import com.github.cnzhoutao.api.LoginApi;
   import com.github.cnzhoutao.dto.UserDTO;
   import org.junit.jupiter.api.Test;
   import org.springframework.boot.test.context.SpringBootTest;
   
   import javax.annotation.Resource;
   
   @SpringBootTest
   class EasyHttpTestApplicationTests {
   
       @Resource
       private JianShuApi jianShuApi;
   
       @Resource
       private LoginApi loginApi;
   
       @Test
       public void testJianshu() {
           String s = jianShuApi.indexPage();
           System.out.println(s);
       }
   
       @Test
       public void testLogin(){
           UserDTO userDTO=new UserDTO("cnzhoutao","github");
           String result = loginApi.login(userDTO);
           System.out.println(result);
       }
   
   }
   ```

   

5. 返回结果

   ![image-20210210124535800](https://tva1.sinaimg.cn/large/008eGmZEly1gnicaf45fcj30ki0800sw.jpg)

![image-20210210124603954](https://tva1.sinaimg.cn/large/008eGmZEly1gnicawjb2yj30ra03a74k.jpg)



####3. header和cookie如何添加

很简单，只需要在需要操作的接口上添加，@CookieHandler 或@HeaderHandler 注解就好了

![image-20210210125029824](https://tva1.sinaimg.cn/large/008eGmZEly1gnicfiruhuj31mo0jsads.jpg)

CookieHandler和HeaderHandler会规格自提供一个map,只需要将想要添加的header或cookie放入相应的map即可

![image-20210210125252849](https://tva1.sinaimg.cn/large/008eGmZEly1gnici09duuj319u0nijub.jpg)

### 特别注意：因为cookie和header是存放在ThreadLocal中，在api使用结束时，需要手动清理

![image-20210210125423365](https://tva1.sinaimg.cn/large/008eGmZEly1gnicjks99tj31dy0do76g.jpg)

#### 测试项目地址

```
https://github.com/cnzhoutao/easy-http-test
```

