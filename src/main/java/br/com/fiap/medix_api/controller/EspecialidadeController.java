package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.response.RespostaEspecialidadeDto;
import br.com.fiap.medix_api.model.Especialidade;
import br.com.fiap.medix_api.service.EspecialidadeService;
import br.com.fiap.medix_api.service.ModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID; // Import adicionado
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades (CRUD via PL/SQL)", description = "Gerenciamento de especialidades médicas (CRUD implementado via Procedures Oracle para demonstração).")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;
    private final ModelMapper modelMapper;

    // --- C - CREATE (via PROCEDURE) ---
    @PostMapping
    @Operation(summary = "Criar nova especialidade (via Procedure)")
    public ResponseEntity<RespostaEspecialidadeDto> criar(@RequestBody Especialidade especialidade, UriComponentsBuilder uriBuilder) {
        Especialidade novaEspecialidade = especialidadeService.criar(especialidade.getNome());
        URI uri = uriBuilder.path("/especialidades/{id}").buildAndExpand(novaEspecialidade.getId()).toUri();

        RespostaEspecialidadeDto dto = modelMapper.mapEspecialidadeToDto(novaEspecialidade);
        dto.add(linkTo(methodOn(EspecialidadeController.class).buscar(dto.getId())).withSelfRel());

        return ResponseEntity.created(uri).body(dto);
    }

    // --- R - READ ALL (via JPA) ---
    @GetMapping
    @Operation(summary = "Listar todas as especialidades")
    public ResponseEntity<List<RespostaEspecialidadeDto>> listar() {
        // CORREÇÃO: Chama o método listarTodas() do serviço.
        List<Especialidade> especialidades = especialidadeService.listarTodas();

        List<RespostaEspecialidadeDto> dtos = especialidades.stream().map(modelMapper::mapEspecialidadeToDto).toList();
        dtos.forEach(dto -> dto.add(linkTo(methodOn(EspecialidadeController.class).buscar(dto.getId())).withSelfRel()));

        return ResponseEntity.ok(dtos);
    }

    // --- R - READ ONE (via JPA) ---
    @GetMapping("/{id}")
    @Operation(summary = "Buscar especialidade por ID")
    public ResponseEntity<RespostaEspecialidadeDto> buscar(@PathVariable Long id) {
        Especialidade especialidade = especialidadeService.buscarPorId(id);
        RespostaEspecialidadeDto dto = modelMapper.mapEspecialidadeToDto(especialidade);
        dto.add(linkTo(methodOn(EspecialidadeController.class).buscar(dto.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    // --- U - UPDATE (via PROCEDURE) ---
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar especialidade (via Procedure)")
    public ResponseEntity<RespostaEspecialidadeDto> atualizar(@PathVariable Long id, @RequestBody Especialidade especialidade) {
        Especialidade atualizada = especialidadeService.atualizar(id, especialidade.getNome());
        RespostaEspecialidadeDto dto = modelMapper.mapEspecialidadeToDto(atualizada);
        dto.add(linkTo(methodOn(EspecialidadeController.class).buscar(dto.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    // --- D - DELETE (via PROCEDURE - Hard Delete) ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir especialidade (via Procedure - DELETE físico)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        especialidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // --- DEMO ENDPOINTS PARA O ENUNCIADO (2x CUD) ---

    @PostMapping("/demo-procedures/insert")
    @Operation(summary = "DEMO: Realiza 2 INSERTS na Especialidade via Procedure", description = "Cria duas novas especialidades (PROC-Especialidade-A e B). Usa um UUID para garantir nomes únicos.")
    public ResponseEntity<String> demoCriarProcedures() {
        // CORREÇÃO: Uso de UUID para garantir unicidade e evitar 409
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        // Chamada 1 de 2: INSERT
        especialidadeService.criar("PROC-Especialidade-A-" + uuid);
        // Chamada 2 de 2: INSERT
        especialidadeService.criar("PROC-Especialidade-B-" + uuid);

        return ResponseEntity.ok("2 Especialidades (PROC-Especialidade-A-" + uuid + " e B-" + uuid + ") inseridas via Procedures. Verifique /especialidades.");
    }

    @PatchMapping("/demo-procedures/update")
    @Operation(summary = "DEMO: Realiza 2 UPDATES na Especialidade via Procedure", description = "Atualiza o nome das especialidades com os IDs informados para 'PROC-UPDT-X'.")
    public ResponseEntity<String> demoAtualizarProcedures(@RequestParam Long id1, @RequestParam Long id2) {
        // Chamada 1 de 2: UPDATE
        especialidadeService.atualizar(id1, "PROC-UPDT-A");
        // Chamada 2 de 2: UPDATE
        especialidadeService.atualizar(id2, "PROC-UPDT-B");

        return ResponseEntity.ok("Especialidades ID " + id1 + " e " + id2 + " atualizadas via Procedures. Verifique /especialidades.");
    }

    @DeleteMapping("/demo-procedures/delete")
    @Operation(summary = "DEMO: Realiza 2 DELETES (Hard) na Especialidade via Procedure", description = "Deleta as especialidades com os IDs informados (DELETE físico).")
    public ResponseEntity<Void> demoExcluirProcedures(@RequestParam Long id1, @RequestParam Long id2) {
        // Chamada 1 de 2: DELETE (Hard Delete)
        especialidadeService.excluir(id1);
        // Chamada 2 de 2: DELETE (Hard Delete)
        especialidadeService.excluir(id2);

        return ResponseEntity.noContent().build();
    }
}