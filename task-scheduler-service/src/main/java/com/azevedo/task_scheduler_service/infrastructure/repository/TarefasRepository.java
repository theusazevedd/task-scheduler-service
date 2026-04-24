package com.azevedo.task_scheduler_service.infrastructure.repository;


import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefasRepository extends MongoRepository<TarefasEntity, String> {


}
