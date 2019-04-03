package com.lzw.swagger;

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
                //设置作者联系方式
                .contact("jamesluozhiwei")
                //版本号
                .version("1.0.0")
                .build();
    }

}
