package pl.newicom.axon.coupon.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Optional;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CommandExecutionException.class)
    public ProblemDetail handleCommandExecutionException(
            CommandExecutionException ex,
            HttpServletRequest request) {

        Optional<Object> details = ex.getDetails();
        if (details.isPresent() && details.get() instanceof ProblemDetail) {
            return (ProblemDetail) details.get();
        } else {
            ProblemDetail fallback = ProblemDetail.forStatus(500);
            fallback.setTitle("Unknown server error");
            fallback.setDetail("Unknown server error");
            fallback.setType(URI.create("urn:unknownServerError"));
            return fallback;
        }
    }
}
