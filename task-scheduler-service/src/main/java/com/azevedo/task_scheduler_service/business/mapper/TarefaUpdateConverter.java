package com.azevedo.task_scheduler_service.business.mapper;

import com.azevedo.task_scheduler_service.business.dto.TarefaRequestDTO;
import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaUpdateConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "emailUsuario", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateTarefas(TarefaRequestDTO dto, @MappingTarget TarefasEntity entity);

}
