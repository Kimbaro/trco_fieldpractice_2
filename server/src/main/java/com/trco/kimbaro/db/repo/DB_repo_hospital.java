package com.trco.kimbaro.db.repo;

import com.trco.kimbaro.db.domain.DB_Object_Hospital;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

@Qualifier("hospital")
public interface DB_repo_hospital extends ReactiveCrudRepository<DB_Object_Hospital, String> {
    Flux<DB_Object_Hospital> findByArea(String area);

}
