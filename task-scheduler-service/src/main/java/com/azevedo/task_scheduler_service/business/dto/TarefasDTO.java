package com.azevedo.task_scheduler_service.business.dto;

import com.azevedo.task_scheduler_service.infrastructure.enums.StatusNotificacaoEnum;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefasDTO {

    private String id;
    private String nomeTarefa;
    private String descricao;
    private Instant dataCriacao;
    private Instant dataEvento;
    private String emailUsuario;
    private Instant dataAlteracao;
    private StatusNotificacaoEnum statusNotificacaoEnum;
}
