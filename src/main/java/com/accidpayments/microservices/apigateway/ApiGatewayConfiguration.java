package com.accidpayments.microservices.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        Function<PredicateSpec, Buildable<Route>> routeFunction
                = p -> p.path("/get")
                        .filters( f -> f
                                .addRequestHeader("MyHeader", "MyUri")
                                .addRequestParameter("param","Myvalue"))
                        .uri("http://httpbin.org:80");

        return builder.routes()
               // .route(routeFunction)
                .route( p -> p.path("/payment/**")
                        .uri("lb://payment-card-service"))//redirect to specific eureka register services
                .route( p -> p.path("/myPayments/**")
                        .filters(f->f.rewritePath(
                                "/myPayments",
                                "/payment"
                                ))
                        .uri("lb://payment-card-service"))//redirect to specific eureka register services
                .build();
    }
}
