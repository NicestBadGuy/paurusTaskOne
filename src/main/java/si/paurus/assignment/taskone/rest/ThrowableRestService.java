package si.paurus.assignment.taskone.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import si.paurus.assignment.taskone.exception.ValidationException;

import java.util.List;
import java.util.UUID;

@ControllerAdvice
@Slf4j
public class ThrowableRestService {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> validationException(final ValidationException exception) {
        return ResponseEntity.badRequest().body(exception.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                       .getFieldErrors()
                       .stream()
                       .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                       .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(final Exception ex) {
        var uuid = UUID.randomUUID().toString();
        log.error("uuid: {} \nmessage: {}", uuid, ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(uuid);
    }
}
