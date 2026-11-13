package com.biblioteca.excepciones;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
@RestControllerAdvice
public class ManejadorGlobalExcepciones {
    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<String> handleLibroNoEncontrado(LibroNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(LibroDuplicadoException.class)
    public ResponseEntity<String> handleLibroDuplicado(LibroDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(LibroPrestadoException.class)
    public ResponseEntity<String> handleLibroPrestado(LibroPrestadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> handleUsuarioNoEncontrado(UsuarioNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(UsuarioDuplicadoException.class)
    public ResponseEntity<String> handleUsuarioDuplicado(UsuarioDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(UsuarioInactivoException.class)
    public ResponseEntity<String> handleUsuarioInactivo(UsuarioInactivoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(UsuarioConPrestamosException.class)
    public ResponseEntity<String> handleUsuarioConPrestamos(UsuarioConPrestamosException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(PrestamoNoEncontradoException.class)
    public ResponseEntity<String> handlePrestamoNoEncontrado(PrestamoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacion(MethodArgumentNotValidException e) {
        Map<String, String> errores = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidation(HandlerMethodValidationException e) {
        Map<String, String> errores = new HashMap<>();
        for (ParameterValidationResult result : e.getParameterValidationResults()) {
            String paramName = result.getMethodParameter().getParameterName();
            for (MessageSourceResolvable error : result.getResolvableErrors()) {
                String key = paramName != null ? paramName : "campo";
                errores.put(key, error.getDefaultMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
}
