package com.chris.uniconnect.Exceptions;

import com.chris.uniconnect.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    //valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                         WebRequest webRequest) {
        Map<String, String> mapErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String clave = ((FieldError) error).getField();
            String valor = error.getDefaultMessage();
            mapErrors.put(clave, valor);
        });
        ApiResponse apiResponse = new ApiResponse(mapErrors.toString(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);

    }

    //personalizada
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourcesNonFoundException(ResourceNotFoundException exception,
                                                                         WebRequest webRequest) {

        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    //para los try
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handlerBadRequestException(BadRequestException exception,
                                                                  WebRequest webRequest) {

        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handlerAuthenticationException(AuthenticationException exception,
                                                                       WebRequest webRequest) {

        String mensaje = exception instanceof BadCredentialsException
                ? "Usuario o contraseña invalidos"
                : exception.getMessage();
        ApiResponse apiResponse = new ApiResponse(mensaje, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    //p.ej. ?rol=FOO o ?page=abc: el parametro no se puede convertir al tipo esperado
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handlerTypeMismatch(MethodArgumentTypeMismatchException exception,
                                                           WebRequest webRequest) {
        String mensaje = "Valor invalido para el parametro '" + exception.getName() + "': " + exception.getValue();
        ApiResponse apiResponse = new ApiResponse(mensaje, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerException(Exception exception,
                                                        WebRequest webRequest) {

        log.error("Error no controlado en {}", webRequest.getDescription(false), exception);
        ApiResponse apiResponse = new ApiResponse("Ocurrio un error interno, intenta de nuevo mas tarde.",
                webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
