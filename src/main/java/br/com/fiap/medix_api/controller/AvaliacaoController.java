package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarAvaliacaoDto;
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

    // Registrar nova avaliação (uso: IoT/Totem)
    @Operation(
            summary = "Registrar nova avaliação",
            description = "Recebe uma nova avaliação vinda de um totem ou interface do usuário.",
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
    public ResponseEntity<Avaliacao> receberAvaliacao(
            @RequestBody @Valid CadastrarAvaliacaoDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        Avaliacao novaAvaliacao = avaliacaoService.registrarAvaliacao(dto);
        novaAvaliacao.add(linkTo(methodOn(AvaliacaoController.class).buscar(novaAvaliacao.getId())).withSelfRel());

        URI uri = uriBuilder.path("/avaliacoes/{id}").buildAndExpand(novaAvaliacao.getId()).toUri();
        return ResponseEntity.created(uri).body(novaAvaliacao);
    }

    // Listar avaliações
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
    @GetMapping
    public ResponseEntity<List<Avaliacao>> listar(@RequestParam(required = false) String status) {
        List<Avaliacao> avaliacoes = avaliacaoService.listar(status);
        avaliacoes.forEach(av -> av.add(linkTo(methodOn(AvaliacaoController.class).buscar(av.getId())).withSelfRel()));
        return ResponseEntity.ok(avaliacoes);
    }

    // Buscar avaliação por ID
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
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscar(@PathVariable Long id) {
        Avaliacao avaliacao = avaliacaoService.buscarPorId(id);
        avaliacao.add(linkTo(methodOn(AvaliacaoController.class).buscar(id)).withSelfRel());
        avaliacao.add(linkTo(methodOn(AvaliacaoController.class).excluir(id)).withRel("excluir"));
        avaliacao.add(linkTo(methodOn(AvaliacaoController.class).listar(null)).withRel("todas"));
        return ResponseEntity.ok(avaliacao);
    }

    // Excluir avaliação (lógica)
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}