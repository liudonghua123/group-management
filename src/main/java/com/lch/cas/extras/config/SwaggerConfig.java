package com.lch.cas.extras.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
//see https://github.com/springfox/springfox/blob/master/docs/asciidoc/getting_started.adoc#springfox-spring-data-rest
//@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(Pageable.class)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(Predicates.not(PathSelectors.regex("/error.*|/oauth/error|/oauth/confirm_access|/oauth/check_token|/oauth/authorize|/oauth/token_key")))
                .build()
                .tags(new Tag("api", "API"), tags())
                .apiInfo(apiInfo())
                .protocols(protocols())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private Tag[] tags() {
        List<Tag> tags = new ArrayList<>();
        return tags.toArray(new Tag[tags.size()]);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Swagger Documentation")
                .description("This is API Swagger Documentation for this project")
                .termsOfServiceUrl("http://liudonghua.com")
                .contact(new Contact("liudonghua", "liudonghua.com", "liudonghua123@gmail.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("2.0")
                .build();
    }

    private Set<String> protocols() {
        Set<String> protocols = new HashSet<>();
        protocols.add("http");
        protocols.add("https");
        return protocols;
    }

    private List<? extends SecurityScheme> securitySchemes() {
        List<SecurityScheme> authorizationTypes = Arrays.asList(new ApiKey("token", "token", "query"));
        return authorizationTypes;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = Arrays.asList(SecurityContext.builder().forPaths(Predicates.not(PathSelectors.regex("^(/error.*|/user/.*|/oauth/token|)$"))).securityReferences(securityReferences()).build());
        return securityContexts;
    }

    private List<SecurityReference> securityReferences() {
        List<SecurityReference> securityReferences = Arrays.asList(SecurityReference.builder().reference("token").scopes(new AuthorizationScope[0]).build());
        return securityReferences;
    }

}