package com.trco.kimbaro.controller;

import com.trco.kimbaro.db.domain.DB_Object_user;
import com.trco.kimbaro.db.repo.DB_repo_user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Controller
public class Manager_Rest_Controller {

    @Autowired
    @Qualifier("user")
    DB_repo_user db_repo_user;
    
}
