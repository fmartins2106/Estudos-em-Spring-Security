package new_projetct.forun_hub.domain.resposta;

import jakarta.transaction.Transactional;
import new_projetct.forun_hub.domain.topicos.Status;
import new_projetct.forun_hub.domain.topicos.TopicoService;
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
    public Resposta cadastrarResposta(Long id, DadosCadastroResposta dadosCadastroResposta){
        var topico = topicoService.pesquisaTopicoPorID(id);
        if (!topico.estaAberto()){
            throw new ValidacaoRegraDeNegocio("Resposta não pode ser mais inserida no tópico. Tópico solucionado.");
        }
        if (topico.getQuantidadeRespostas() == 0){
            topico.alterarStatus(Status.RESPONDIDO);
        }
        topico.encrementarResposta();
        var resposta = new Resposta(dadosCadastroResposta);
        respostaRepository.save(resposta);
        return resposta;
    }

    @Transactional
    public Resposta pesquisaRespostaID(Long idResposta){
        return respostaRepository.findById(idResposta)
                .orElseThrow(() -> new RuntimeException("Erro. ID não encontrado."));
    }

    @Transactional
    public Resposta alterarResposta(Long id, DadosAtualizacaoResposta dadosAtualizacaoResposta){
        var resposta = pesquisaRespostaID(id);
        resposta.atualizarDadosResposta(dadosAtualizacaoResposta);
        respostaRepository.save(resposta);
        return resposta;
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
