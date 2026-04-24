package com.azevedo.task_scheduler_service.infrastructure.repository;


import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TarefasRepository extends MongoRepository<TarefasEntity, String> {

    List<TarefasEntity> findByDataEventoBetween(Instant dataInicio, Instant dataFim);

    List<TarefasEntity> findByEmailUsuario(String email);


}
