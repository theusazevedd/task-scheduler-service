package com.azevedo.task_scheduler_service.business.service;

import com.azevedo.task_scheduler_service.business.dto.TarefasDTO;
import com.azevedo.task_scheduler_service.business.mapper.TarefasConverter;
import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import com.azevedo.task_scheduler_service.infrastructure.enums.StatusNotificacaoEnum;
import com.azevedo.task_scheduler_service.infrastructure.repository.TarefasRepository;
import com.azevedo.task_scheduler_service.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setEmailUsuario(email);
        dto.setDataCriacao(java.time.Instant.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));


    }

}
