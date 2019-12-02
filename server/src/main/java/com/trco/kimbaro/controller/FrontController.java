package com.trco.kimbaro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class FrontController {

    @Bean
    public RouterFunction<ServerResponse> staticRouterFunction() {
        return RouterFunctions.resources("/**", new ClassPathResource("/public"));
    }


    @Bean
    public RouterFunction<ServerResponse> mainRouterFuction(@Value("classpath:/static/index.html") Resource index) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(index));
    }
    @Bean
    public RouterFunction<ServerResponse> address_searchRouterFuction(@Value("classpath:/static/address_search/index.html") Resource index) {
        return route(GET("/address_search"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(index));
    }
    @Bean
    public RouterFunction<ServerResponse> hospital_mapRouterFuction(@Value("classpath:/static/hospital_map/index.html") Resource index) {
        return route(GET("/hospital_map"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(index));
    }
}
