package com.azevedo.task_scheduler_service.business.mapper;

import com.azevedo.task_scheduler_service.business.dto.TarefaRequestDTO;
import com.azevedo.task_scheduler_service.business.dto.TarefaResponseDTO;
import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefasConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "emailUsuario", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "status", ignore = true)
    TarefasEntity paraTarefaEntity(TarefaRequestDTO dto);

    TarefaResponseDTO paraTarefaResponseDTO(TarefasEntity entity);

    List<TarefaResponseDTO> paraListaTarefasResponseDTO(List<TarefasEntity> entities);
}
