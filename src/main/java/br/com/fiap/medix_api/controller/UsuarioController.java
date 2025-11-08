// challengeoracle/sprint-1-java/sprint-1-java-bcdd9f4fff0dc8486f32bc0e866a54c1f21ef0c2/src/main/java/br/com/fiap/medix_api/controller/UsuarioController.java (Novo Arquivo)
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.response.RespostaColaboradorDto;
import br.com.fiap.medix_api.dto.response.RespostaPacienteDto;
import br.com.fiap.medix_api.dto.response.RespostaUsuarioDto;
import br.com.fiap.medix_api.enums.UsuarioRole;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.model.Usuario;
import br.com.fiap.medix_api.repository.ColaboradorRepository;
import br.com.fiap.medix_api.repository.PacienteRepository;
import br.com.fiap.medix_api.repository.UsuarioRepository;
import br.com.fiap.medix_api.service.ModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para informações do usuário logado.")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
    @Operation(
            summary = "Buscar informações do usuário logado (Paciente ou Colaborador)",
            description = "Retorna os dados completos do usuário logado, com base no seu tipo (Role). O Mobile deve verificar o campo 'tipoUsuario' para saber a estrutura da resposta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário encontrados.",
                            content = @Content(schema = @Schema(oneOf = {RespostaPacienteDto.class, RespostaColaboradorDto.class}))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado no banco de dados."),
                    @ApiResponse(responseCode = "403", description = "Não autenticado.")
            }
    )
    public ResponseEntity<RespostaUsuarioDto> buscarUsuarioLogado() {
        // 1. Extrai o e-mail (username) do token JWT no contexto de segurança
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Busca o usuário base (polimórfico) pelo e-mail
        Usuario usuario = usuarioRepository.findByEmailAndDeletedIs(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado no sistema."));

        // 3. Verifica o tipo e busca a subclasse (Paciente ou Colaborador)
        if (usuario.getTipoUsuario() == UsuarioRole.PACIENTE) {
            Paciente paciente = pacienteRepository.findByIdAndDeletedIs(usuario.getId(), 0)
                    .orElseThrow(() -> new EntityNotFoundException("Dados de paciente não encontrados."));
            RespostaPacienteDto dto = modelMapper.mapPacienteToDto(paciente);
            dto.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioLogado()).withSelfRel());
            return ResponseEntity.ok(dto);
        }

        if (usuario.getTipoUsuario() == UsuarioRole.COLABORADOR) {
            Colaborador colaborador = colaboradorRepository.findByIdAndDeletedIs(usuario.getId(), 0)
                    .orElseThrow(() -> new EntityNotFoundException("Dados de colaborador não encontrados."));
            RespostaColaboradorDto dto = modelMapper.mapColaboradorToDto(colaborador);
            dto.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioLogado()).withSelfRel());
            return ResponseEntity.ok(dto);
        }

        // Tipo desconhecido/não mapeado
        throw new EntityNotFoundException("Tipo de usuário não reconhecido.");
    }
}