package pl.newicom.axon;

import java.net.URI;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class AxonCommandMessageInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

	private final ConversionService conversionService;

	public AxonCommandMessageInterceptor(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
		try {
			return interceptorChain.proceed();
		} catch (Throwable ex) {
			throw new CommandExecutionException(ex.getMessage(), ex, toHttpProblemDetail(ex));
		}
	}


	private ProblemDetail toHttpProblemDetail(Throwable ex) {
		if (conversionService.canConvert(ex.getClass(), ProblemDetail.class)) {
			return conversionService.convert(ex, ProblemDetail.class);
		} else {
			ProblemDetail problem = ProblemDetail.forStatus(HttpStatusCode.valueOf(500));
			problem.setTitle("Internal Server Error");
			problem.setDetail("An unexpected error occurred while processing the request: " + ex.getMessage());
			problem.setType(URI.create("urn:problem-type:internal-server-error"));
			return problem;
		}
	}
}
