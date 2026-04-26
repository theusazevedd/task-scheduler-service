package com.azevedo.task_scheduler_service.infrastructure.repository;


import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import com.azevedo.task_scheduler_service.infrastructure.enums.StatusNotificacaoEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TarefasRepository extends MongoRepository<TarefasEntity, String> {

    List<TarefasEntity> findByDataEventoBetweenAndStatus(Instant dataInicio,
                                                         Instant dataFim,
                                                         StatusNotificacaoEnum status);

    List<TarefasEntity> findByEmailUsuario(String email);


}
