package com.trco.kimbaro.db.repo;

import com.trco.kimbaro.db.domain.DB_Object_Pulmonary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

@Qualifier("pulmonary")
public interface DB_repo_pulmonary extends ReactiveCrudRepository<DB_Object_Pulmonary, String> {
    Flux<DB_Object_Pulmonary> findByUserIdAndType(String userId, int type);

    Flux<DB_Object_Pulmonary> findByUserId(String userId);
}
