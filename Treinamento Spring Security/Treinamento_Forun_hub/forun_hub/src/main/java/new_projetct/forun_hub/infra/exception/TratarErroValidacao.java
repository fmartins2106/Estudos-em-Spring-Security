package new_projetct.forun_hub.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.rmi.AccessException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class TratarErroValidacao {

    private record DadosErroValidacao(String campo, String mensagem){
        private DadosErroValidacao(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

    private record ErroResponse(int status, String erro, String mensagem, LocalDateTime timeStamp){

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarErro400(MethodArgumentNotValidException exception){
        var erro = exception.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErroResponse> tratarErro400JsonInvalido(HttpMessageConversionException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(400, "Bad Request", "Json mal formatado ou campo inválido",
                        LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarErro400ParamentroInvalido(MethodArgumentTypeMismatchException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(400, "Bad Request", "Paramentro iválido."+exception.getMessage(),
                        LocalDateTime.now()));
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ErroResponse> tratarErro401(BadCredentialsException exception){
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(new ErroResponse(401,"Unauthorized",
//                        "Login ou senha inválidos", LocalDateTime.now()));
//    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<ErroResponse> tratarErro403(AccessException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroResponse(403, "Forbidden", "Acesso negado.",
                        LocalDateTime.now()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> tratarErro404(EntityNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(404, "Not Found", exception.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErroResponse> tratarErro404RotaNaoEncontrada(NoHandlerFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(400, "Not Found",
                        "Rota não encontrada."+ exception.getRequestURL(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarErro409(DataIntegrityViolationException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(409, "Conflict",
                        "Erro de violação de dados.", LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErro500(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(500, "Internal Server Erro",
                        "Erro inesperado"+exception.getMessage()
                , LocalDateTime.now()));
    }










}
