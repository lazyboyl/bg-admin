package com.github.bg.admin.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：接口测试工具的配置
 *
 * @author linzf
 * @since 2019-08-26
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        //header中的ticket参数非必填，传空也可以
        ticketPar.name("x-access-token").description("认证token的值")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        //根据每个方法名也知道当前方法在设置什么参数
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("用户权限架构的接口测试中心")
                //创建人
                .contact(new Contact("linzef", "", "282245889@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("用户权限架构的接口测试专用API文档")
                .build();
    }

}
