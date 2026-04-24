package com.azevedo.task_scheduler_service.business.mapper;

import com.azevedo.task_scheduler_service.business.dto.TarefasDTO;
import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefasConverter {

    TarefasEntity paraTarefaEntity(TarefasDTO dto);

    TarefasDTO paraTarefaDTO(TarefasEntity entity);

}
