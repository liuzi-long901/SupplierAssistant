package cn.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();

        // JWT Header
        Parameter jwtParameter = parameterBuilder
                .parameterType("header")
                .name("Jwt-Authorization")
                .defaultValue("")
                .description("Jwt-Authorization Header")
                .modelRef(new ModelRef("string"))
                .required(false).build();

        parameters.add(jwtParameter);

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.cloud.controller"))
                .paths(PathSelectors.any())
                .build().securitySchemes(securitySchemes());
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("Jwt-Authorization", "Jwt-Authorization", "header"));
        return apiKeyList;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("cloud.message web")
                .version("1.0")
                .build();
    }
}
