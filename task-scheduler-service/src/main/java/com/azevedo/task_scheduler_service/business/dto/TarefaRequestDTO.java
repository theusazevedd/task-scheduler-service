package com.azevedo.task_scheduler_service.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaRequestDTO {

    private String nomeTarefa;
    private String descricao;
    private Instant dataEvento;
}
