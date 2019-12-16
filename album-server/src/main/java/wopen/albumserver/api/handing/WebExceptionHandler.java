package wopen.albumserver.api.handing;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wopen.albumserver.app.exception.HttpException;
import wopen.albumserver.infra.i18n.MessageTranslator;
import wopen.albumserver.infra.i18n.Messages;

@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageTranslator messageTranslator;

    public WebExceptionHandler(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        return handleExceptionInternal(ex, ApiError.of(ex.getLocalizedMessage()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({HttpException.class})
    public ResponseEntity<Object> handleHttpException(HttpException ex) {
        return new ResponseEntity<>(ApiError.of(messageTranslator.translate(ex.getMessage())), ex.getStatus());
    }

    @ExceptionHandler({HttpException.class})
    public ResponseEntity<Object> handleAll() {
        return new ResponseEntity<>(ApiError.of(messageTranslator.translate(Messages.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
