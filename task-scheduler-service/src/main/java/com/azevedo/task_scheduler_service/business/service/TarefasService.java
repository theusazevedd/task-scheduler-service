package com.azevedo.task_scheduler_service.business.service;

import com.azevedo.task_scheduler_service.business.dto.TarefasDTO;
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

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setEmailUsuario(email);
        dto.setDataCriacao(java.time.Instant.now());
        dto.setStatus(StatusNotificacaoEnum.PENDENTE);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));

    }

    public List<TarefasDTO> buscaTarefasAgendasPorPeriodo(Instant dataInicial, Instant dataFinal) {
        return tarefaConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetweenAndStatus(dataInicial, dataFinal,
                        StatusNotificacaoEnum.PENDENTE));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);
        return tarefaConverter.paraListaTarefasDTO(listaTarefas);
    }

    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente" + id, e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada por id " + id));

            entity.setStatus(status);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada por id " + id));

            tarefaUpdateConverter.updateTarefas(dto, entity);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao atualizar tarefa " + e.getCause());
        }

    }


}