[TOC]
## Swagger介绍
&emsp;&emsp;swagger是一个规范和完整的框架，用于生成、描述、调用的RESTful风格API文档。总体来说，是让前后端的API文档始终保存同步。
&emsp;&emsp;我们的RESTful API就有可能要面对多个开发人员或多个开发团队：IOS开发、Android开发或是Web开发等。为了减少与其他团队平时开发期间的频繁沟通成本，传统做法我们会创建一份RESTful API文档来记录所有接口细节，然而这样的做法有以下几个问题：

 - 由于接口众多，并且细节复杂（需要考虑不同的HTTP请求类型、HTTP头部信息、HTTP请求内容等），高质量地创建这份文档本身就是件非常吃力的事，下游的抱怨声不绝于耳。
- 随着时间推移，不断修改接口实现的时候都必须同步修改接口文档，而文档与代码又处于两个不同的媒介，除非有严格的管理机制，不然很容易导致不一致现象。

&emsp;&emsp;为了解决上面这样的问题，本文将介绍RESTful API的重磅好伙伴Swagger2，它可以轻松的整合到Spring Boot中，并与Spring MVC程序配合组织出强大RESTful API文档。它既可以减少我们创建文档的工作量，同时说明内容又整合入实现代码中，让维护文档和修改代码整合为一体，可以让我们在修改代码逻辑的同时方便的修改文档说明。另外Swagger2也提供了强大的页面测试功能来调试每个RESTful API。具体效果如下图所示(本文使用的版本是2.9.2)
![swaggerAPI文档图](https://img-blog.csdnimg.cn/20190403104420158.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
下面开始介绍如何在springboot中使用swagger(springMVC的话有个大坑，后续说明);

## 1、添加依赖
 &emsp;如何创建boot项目就不阐述了。在pom.xml中加入swagger的依赖
 ```xml
 		<!-- 添加Swagger2依赖 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
 ```
 ## 2、创建swagger的配置类
 ```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author James
 * Swagger配置类，请确保当前类可以被扫描到
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * createRestApi函数用于创建Docket的Bean
     * apiInfo()用来创建该Api的基本信息（这些基本信息会展现在文档页面中）
     * select()函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义，
     * Swagger会扫描该包下所有Controller定义的API，并产生文档内容（除了被@ApiIgnore指定的请求）
     * @return
     */
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指向自己需扫描的包，一般指向 返回实体和控制层
                .apis(RequestHandlerSelectors.basePackage("com.lzw.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 设置页面显示信息
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                //页面标题
                .title("springboot使用swagger构建restful API")
                //描述
                .description("更多资料请查看：https://www.cqwxhn.xin")
                //版本号
                .version("1.0.0")
                .build();
    }

}

```
通过<font color="Chocolate">@Configuration</font>注解让spring加载当前配置类。通过<font color="Chocolate">@EnableSwagger2</font>注解开启swaggerAPI文档。
## 3、controller添加注解
```java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author James
 * 测试控制层
 */
@Api(tags = "TestController",description = "控制层描述")
@RestController
public class TestController {

    /**
     * 测试接口
     * @param id
     * @return 返回自定义 统一响应消息对象
     */
    @ApiOperation(value = "测试接口",notes = "用于测试swagger的restful API文档")
    @RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public ApiResponseObject test(@PathVariable("id")int id){
        return new ApiResponseObject().setData(id);
    }

    /**
     * POST测试
     * @param params
     * @return 返回自定义 统一响应消息对象
     */
    @ApiOperation(value = "POST测试",notes = "用于测试使用实体作为参数的效果，通过produces指定contentType",produces = "application/json;charset=utf-8")
    @RequestMapping(value = "/save/XXX",method = RequestMethod.POST)
    public ApiResponseObject saveXXX(@RequestBody ApiResponseObject params){
        return new ApiResponseObject().setData(params);
    }

    /**
     * 多参数测试
     * @param code
     * @param msg
     * @return 返回自定义 统一响应消息对象
     */
    @ApiOperation(value = "多参数测试",notes = "用于测试多个参数的效果")
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value = "响应code",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name="msg",value = "响应msg",required = true,dataType = "string",paramType = "query")
    })
    @RequestMapping(value = "/test/params",method = RequestMethod.PUT)
    public ApiResponseObject testParams(int code,String msg){
        return new ApiResponseObject().setCode(code).setMsg(msg);
    }

}

```
在controller添加<font color="Chocolate">@Api</font>注解后就会扫描该控制类下面的映射并给出相应的文档内容，我在控制层里面添加了一个实体，用于统一响应消息，使用了<font color="Chocolate">@ApiModel</font>注解，代码如下
```java
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author James
 * 统一API响应类
 */
@ApiModel
public class ApiResponseObject {

    @ApiModelProperty(value = "响应标识")
    private int code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private Object data;

    public ApiResponseObject() {
        this.code = 0;
        this.msg = "test";
        this.data = null;
    }

    public ApiResponseObject(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public ApiResponseObject setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResponseObject setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ApiResponseObject setData(Object data) {
        this.data = data;
        return this;
    }
}

```
添加实体后就可以启动项目了，访问:http://localhost:{{port}}/{{项目上下文}}/swagger-ui.html;
eg:http://localhost:8080/swagger-ui.html
访问就可以看到api文档了，简直详细的不得了
## 浏览API
展开某一个具体的接口就可以看到这个接口的信息
![API详情](https://img-blog.csdnimg.cn/20190403111311975.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
点击按钮try it out就可以针对当前api填写参数
![填写API参数](https://img-blog.csdnimg.cn/20190403111439809.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
可以看到参数的类型，是否必填等待信息，填写完毕之后点击 Execute按钮就可以执行请求测试了
![API请求结果](https://img-blog.csdnimg.cn/20190403111555736.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
点击执行按钮之后，会在按钮下方看到一个response body的响应数据，这个就是后台返回的数据了

## 浏览实体
将models展开就可以看到自己使用<font color="Chocolate">@ApiModel</font>定义的实体内容了
![实体图片](https://img-blog.csdnimg.cn/2019040311212761.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
## 传递多个参数
如果需要传递多个参数，同理，在后台设置添加多个参数后使用<font color="chocolate">@ApiImplicitParams</font>及<font color="chocolate">@ApiImplicitParam</font>组合的形式声明

```java
	@ApiImplicitParams({
            @ApiImplicitParam(name="code",value = "响应code",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name="msg",value = "响应msg",required = true,dataType = "string",paramType = "query")
    })
```
![传递多个参数图片](https://img-blog.csdnimg.cn/20190403113002726.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
## 传递实体参数
将多个参数封装成对象进行传递时，用到了实体参数
注意：传递实体参数时，如果将contentType设置成application/json;charset=utf-8的形式进行传递的话，后台注意使用<font color="chocolate">@RequestBody</font>注解
![传递实体参数图片](https://img-blog.csdnimg.cn/20190403112308672.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
swagger 的简单使用到这里接结束了，下面简要介绍一下各个注解的参数及其作用
## 各个注解的解释
```java
	@Api：用在请求的类上，表示对类的说明
	    tags="说明该类的作用，可以在UI界面上看到的注解"
	    value="该参数没什么意义，在UI界面上也看到，所以不需要配置"
	
	@ApiOperation：用在请求的方法上，说明方法的用途、作用
	    value="说明方法的用途、作用"
	    notes="方法的备注说明"
	
	@ApiImplicitParams：用在请求的方法上，表示一组参数说明
	    @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
	        name：参数名
	        value：参数的汉字说明、解释
	        required：参数是否必须传
	        paramType：参数放在哪个地方
	            · header --> 请求参数的获取：@RequestHeader
	            · query --> 请求参数的获取：@RequestParam
	            · path（用于restful接口）--> 请求参数的获取：@PathVariable
	            · body（不常用）
	            · form（不常用）    
	        dataType：参数类型，默认String，其它值dataType="Integer"       
	        defaultValue：参数的默认值
	
	@ApiResponses：用在请求的方法上，表示一组响应
	    @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
	        code：数字，例如400
	        message：信息，例如"请求参数没填好"
	        response：抛出异常的类
	
	@ApiModel：用于响应类上，表示一个返回响应数据的信息
	            （这种一般用在post创建的时候，使用@RequestBody这样的场景，
	            请求参数无法使用@ApiImplicitParam注解进行描述的时候）
	    @ApiModelProperty：用在属性上，描述响应类的属性
```
具体使用例子可以查看上面的<font color="blue">controller</font>和<font color="blue">实体</font>代码
## spring MVC 使用swagger的坑
&emsp;&emsp;首先个人建议如果使用swagger的话就直接用boot了吧，如果要使用SSM的话就要注意咯：
**问题描述:**
&emsp;&emsp;页面提示Failed to load API definition，并且接口（http：//127.0.0.1:8080/XXX/api-docs）可以返回json。
**结论:**
&emsp;&emsp;swagger-ui版本与服务端返回接口json值不兼容，下载低版本swagger-ui即可。可用地址https://github.com/swagger-api/swagger-ui/tree/v2.2.10 下载后将dist目录拷贝到webapps下即可。
**分析:**
&emsp;&emsp;按照网上帖子愉快部署后，发现这个蛋疼问题，搜了下，还没什么答案，后面各种查资料，git上下载别人demo，对比接口地址 http://127.0.0.1:8080/XXX/api-docs 返回值，发现返回的json和可用项目的差不多，并且json格式中有版本信息1.2；然后看了swagger-ui和项目中不一样，后面去swagger-ui上看了下兼容性，地址https://github.com/swagger-api/swagger-ui，明确的写了，ui兼容的版本，网上那些帖子略坑，都是说直接到git上下载后dist拷贝到webapps，从3.x版本ui已经不支持1.2的json格式了，所以最高下载2.2.10才支持springmvc，同时看了下swagger-springmvc这个包，2015年后就没有更新了。所以只要是用springmvc这个包，服务端json必定不超过1.2，所以ui版本只能选兼容1.2版本的。

swagger-ui版本兼容列表，图片：
![swagger-ui版本兼容](https://img-blog.csdnimg.cn/20190403121420187.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
而swagger-springmvc.jar包版本，最新的是2015年的1.0.2版本。
![swagger springmvc 中央仓库版本图片](https://img-blog.csdnimg.cn/20190403121645736.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4NDAzNjYy,size_16,color_FFFFFF,t_70)
既然现在springmvc已经没有更新的包了，哪这些新版本的swagger-ui给谁用。ok，你猜对了，给springboot，springfox这些用喽。
## 项目源码
虽然代码较少，最后还是奉上demo源码：
github:https://github.com/jamesluozhiwei/swaggerTest.git

有疑问和不足之处欢迎留言讨论

博主个人博客传送门：https://www.cqwxhn.xin
