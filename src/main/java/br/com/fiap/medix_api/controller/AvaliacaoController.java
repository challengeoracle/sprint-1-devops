package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.CadastrarAvaliacaoDto;
import br.com.fiap.medix_api.model.Avaliacao;
import br.com.fiap.medix_api.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID; // Import necessário para garantir unicidade na demo

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/avaliacoes")
@AllArgsConstructor
@Tag(
        name = "Avaliações",
        description = "Endpoints relacionados ao recebimento, listagem e gerenciamento de avaliações (público)."
)
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    // NOVO/ATUALIZADO: Endpoint normal para POST (somente para o payload)
    @Operation(
            summary = "Registrar nova avaliação (Normal)",
            description = "Registra uma nova avaliação baseada no payload.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Avaliação registrada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Avaliacao.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos ou incompletos na requisição.",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Avaliacao> receberAvaliacaoNormal(
            @RequestBody @Valid CadastrarAvaliacaoDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        // Apenas continua com a operação normal de cadastro do endpoint
        Avaliacao novaAvaliacao = avaliacaoService.registrarAvaliacao(dto);
        novaAvaliacao.add(linkTo(methodOn(AvaliacaoController.class).buscar(novaAvaliacao.getId())).withSelfRel());

        URI uri = uriBuilder.path("/avaliacoes/{id}").buildAndExpand(novaAvaliacao.getId()).toUri();
        return ResponseEntity.created(uri).body(novaAvaliacao);
    }

    // NOVO ENDPOINT: Dedicado para o INSERT DEMO (2x)
    @PostMapping("/demo-procedures/insert")
    @Operation(
            summary = "DEMO: Realiza 2 INSERTS na Avaliação via Procedure",
            description = "Executa 2 INSERTS via Procedure para demonstração. Usa um UUID para garantir nomes únicos. Use GET /avaliacoes para verificar e anote os IDs."
    )
    public ResponseEntity<String> demoCriarProcedures() {
        // Geramos um UUID para garantir que o setor seja único e evitar 409
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        // [DEMO: INSERT 1 de 2]
        avaliacaoService.demoInsert("PROC-Setor-A-" + uuid, "PROC-Local-A", "EXCELENTE");
        // [DEMO: INSERT 2 de 2]
        avaliacaoService.demoInsert("PROC-Setor-B-" + uuid, "PROC-Local-B", "RUIM");

        return ResponseEntity.ok("2 Avaliações (PROC-Setor-A-" + uuid + " e B-" + uuid + ") inseridas via Procedures. Verifique /avaliacoes e anote os IDs para UPDATE/DELETE.");
    }

    // Listar avaliações
    @GetMapping
    @Operation(
            summary = "Listar avaliações",
            description = "Retorna todas as avaliações registradas. É possível filtrar por status (ativo ou deletado).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de avaliações retornada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Avaliacao.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<Avaliacao>> listar(@RequestParam(required = false) String status) {
        List<Avaliacao> avaliacoes = avaliacaoService.listar(status);
        avaliacoes.forEach(av -> av.add(linkTo(methodOn(AvaliacaoController.class).buscar(av.getId())).withSelfRel()));
        return ResponseEntity.ok(avaliacoes);
    }

    // Buscar avaliação por ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar avaliação por ID",
            description = "Retorna uma avaliação específica com base no ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Avaliação encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Avaliacao.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Avaliação não encontrada.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Avaliacao> buscar(@PathVariable Long id) {
        Avaliacao avaliacao = avaliacaoService.buscarPorId(id);
        avaliacao.add(linkTo(methodOn(AvaliacaoController.class).buscar(id)).withSelfRel());
        avaliacao.add(linkTo(methodOn(AvaliacaoController.class).excluir(id)).withRel("excluir"));
        avaliacao.add(linkTo(methodOn(AvaliacaoController.class).listar(null)).withRel("todas"));
        return ResponseEntity.ok(avaliacao);
    }

    // ENDPOINTS DE DEMONSTRAÇÃO DE PROCEDURE (Avaliação)

    // [DEMO: UPDATE 2x] - Tabela Avaliação
    @PatchMapping("/demo-procedures/update")
    @Operation(summary = "DEMO: Realiza 2 UPDATES na Avaliação via Procedure")
    public ResponseEntity<String> demoAtualizarProcedures(@RequestParam Long id, @RequestParam Long id2) {
        // Chamada 1 de 2: UPDATE
        avaliacaoService.demoUpdate(id, "PROC-Update-Setor-1");
        // Chamada 2 de 2: UPDATE
        avaliacaoService.demoUpdate(id2, "PROC-Update-Setor-2");

        return ResponseEntity.ok("Avaliações ID " + id + " e " + id2 + " atualizadas via Procedures. Verifique /avaliacoes.");
    }

    // [DEMO: DELETE 2x] - Tabela Avaliação
    @DeleteMapping("/demo-procedures/delete")
    @Operation(summary = "DEMO: Realiza 2 DELETES (Soft) na Avaliação via Procedure")
    public ResponseEntity<Void> demoExcluirProcedures(@RequestParam Long id, @RequestParam Long id2) {
        // Chamada 1 de 2: DELETE (Soft Delete)
        avaliacaoService.demoDelete(id);
        // Chamada 2 de 2: DELETE (Soft Delete)
        avaliacaoService.demoDelete(id2);

        return ResponseEntity.noContent().build();
    }

    // Mantendo a rota de DELETE original com o método original
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir avaliação (lógica)",
            description = "Realiza a exclusão lógica de uma avaliação, mantendo o registro no banco, mas marcando como inativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Avaliação excluída com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Avaliação não encontrada.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}