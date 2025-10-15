package new_projetct.forun_hub.domain.resposta;

import jakarta.transaction.Transactional;
import new_projetct.forun_hub.domain.topicos.Status;
import new_projetct.forun_hub.domain.topicos.Topico;
import new_projetct.forun_hub.domain.topicos.TopicoService;
import new_projetct.forun_hub.domain.usuario.Usuario;
import new_projetct.forun_hub.infra.exception.ValidacaoRegraDeNegocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoService topicoService;

    @Transactional
    public Resposta cadastrarResposta(DadosCadastroResposta dadosCadastroResposta, Usuario autor){
        var topico = topicoService.pesquisaTopicoPorID(dadosCadastroResposta.idTopico());
        if (!topico.estaAberto()){
            throw new ValidacaoRegraDeNegocio("Resposta não pode ser mais inserida no tópico. Tópico solucionado.");
        }
        if (topico.getQuantidadeRespostas() == 0){
            topico.alterarStatus(Status.RESPONDIDO);
        }
        topico.encrementarResposta();
        var resposta = new Resposta(dadosCadastroResposta, topico, autor);
        respostaRepository.save(resposta);
        return resposta;
    }


    @Transactional
    public Resposta alterarResposta(DadosAtualizacaoResposta dadosAtualizacaoResposta){
        var resposta = pesquisaRespostaID(dadosAtualizacaoResposta.id());
        resposta.atualizarDadosResposta(dadosAtualizacaoResposta);
        respostaRepository.save(resposta);
        return resposta;
    }

    @Transactional
    public Resposta pesquisaRespostaID(Long idResposta){
        return respostaRepository.findById(idResposta)
                .orElseThrow(() -> new RuntimeException("Erro. ID não encontrado."));
    }

    @Transactional
    public Resposta marcarComoSolucao(Long id){
        var resposta = pesquisaRespostaID(id);
        var topico = resposta.getTopico();
        if (topico.getStatus() == Status.RESOLVIDO){
            throw new ValidacaoRegraDeNegocio("Tópico já foi solucionado.");
        }
        topico.alterarStatus(Status.RESOLVIDO);
        return resposta.marcarComoSolucao();
    }


    @Transactional
    public void excluirResposta(Long id){
        var resposta = pesquisaRespostaID(id);
        var topico = resposta.getTopico();

        respostaRepository.deleteById(id);
        topico.decrementeResposta();

        if (topico.getQuantidadeRespostas() == 0){
            topico.alterarStatus(Status.NAO_RESPONDIDO);
            return;
        }
        topico.alterarStatus(Status.RESPONDIDO);
    }







}
