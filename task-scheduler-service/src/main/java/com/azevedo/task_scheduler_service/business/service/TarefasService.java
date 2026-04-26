package com.azevedo.task_scheduler_service.business.service;

import com.azevedo.task_scheduler_service.business.dto.TarefaRequestDTO;
import com.azevedo.task_scheduler_service.business.dto.TarefaResponseDTO;
import com.azevedo.task_scheduler_service.business.mapper.TarefaUpdateConverter;
import com.azevedo.task_scheduler_service.business.mapper.TarefasConverter;
import com.azevedo.task_scheduler_service.infrastructure.entity.TarefasEntity;
import com.azevedo.task_scheduler_service.infrastructure.enums.StatusNotificacaoEnum;
import com.azevedo.task_scheduler_service.infrastructure.exceptions.ResourceNotFoundException;
import com.azevedo.task_scheduler_service.infrastructure.repository.TarefasRepository;
import com.azevedo.task_scheduler_service.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefaResponseDTO gravarTarefa(String token, TarefaRequestDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        entity.setEmailUsuario(email);
        entity.setDataCriacao(java.time.Instant.now());
        entity.setStatus(StatusNotificacaoEnum.PENDENTE);
        return tarefaConverter.paraTarefaResponseDTO(tarefasRepository.save(entity));

    }

    public List<TarefaResponseDTO> buscaTarefasAgendasPorPeriodo(Instant dataInicial, Instant dataFinal) {
        return tarefaConverter.paraListaTarefasResponseDTO(
                tarefasRepository.findByDataEventoBetweenAndStatus(dataInicial, dataFinal,
                        StatusNotificacaoEnum.PENDENTE));
    }

    public List<TarefaResponseDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);
        return tarefaConverter.paraListaTarefasResponseDTO(listaTarefas);
    }

    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente" + id, e.getCause());
        }
    }

    public TarefaResponseDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada por id " + id));

            entity.setStatus(status);
            return tarefaConverter.paraTarefaResponseDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }

    public TarefaResponseDTO updateTarefas(TarefaRequestDTO dto, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada por id " + id));

            tarefaUpdateConverter.updateTarefas(dto, entity);
            return tarefaConverter.paraTarefaResponseDTO(tarefasRepository.save(entity));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao atualizar tarefa " + e.getCause());
        }

    }


}