package com.trco.kimbaro.db.repo;

import com.trco.kimbaro.db.domain.DB_Object_user;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@Qualifier("user")
public interface DB_repo_user extends ReactiveCrudRepository<DB_Object_user, String> {

    Mono<DB_Object_user> findByTrcoId(String trcoId);

    Mono<DB_Object_user> findByTrcoIdAndTrcoPw(String trcoId, String trcoPw);
}
