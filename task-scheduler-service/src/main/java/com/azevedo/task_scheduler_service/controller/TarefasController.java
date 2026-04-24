package com.azevedo.task_scheduler_service.controller;

import com.azevedo.task_scheduler_service.business.dto.TarefasDTO;
import com.azevedo.task_scheduler_service.business.service.TarefasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tarefas")
public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<TarefasDTO> salvaTarefa(@RequestBody TarefasDTO dto,
                                                  @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.gravarTarefa(token, dto));
    }

}
