package pl.newicom.axon.coupon;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.common.Registration;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.saga.repository.jpa.SagaEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.newicom.axon.AxonCommandMessageInterceptor;
import pl.newicom.axon.countries.ByIPCountryResolver;
import pl.newicom.axon.countries.PolandCountryResolver;
import pl.newicom.axon.web.ClientIPAddressResolver;

@EntityScan(basePackageClasses = {SagaEntry.class, TokenEntry.class})
@SpringBootApplication
@EnableScheduling
public class CouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}

	@Bean
	public AxonCommandMessageInterceptor commandMessageInterceptor(ConversionService conversionService) {
		return new AxonCommandMessageInterceptor(conversionService);
	}

	@Bean
	public ByIPCountryResolver byIPCountryResolver() {
		return new PolandCountryResolver();
	}

	@Bean
	public ClientIPAddressResolver clientIPAddressResolver() {
		return () -> Optional.of("127.0.0.1");
	}

	@Bean
	public CommandGateway commandGateway(CommandBus commandBus, AxonCommandMessageInterceptor commandMessageInterceptor) {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
		IntervalRetryScheduler rs = IntervalRetryScheduler.builder().retryExecutor(scheduledExecutorService).maxRetryCount(5).retryInterval(1000).build();
		//noinspection resource
		commandBus.registerHandlerInterceptor(commandMessageInterceptor);
		return DefaultCommandGateway.builder().commandBus(commandBus).retryScheduler(rs).build();

	}


	@Bean(destroyMethod = "")
	public DeadlineManager deadlineManager(TransactionManager transactionManager,
										   Configuration config) {
		return SimpleDeadlineManager.builder()
				.transactionManager(transactionManager)
				.scopeAwareProvider(new ConfigurationScopeAwareProvider(config))
				.build();
	}

	@Autowired
	public void configureSerializers(ObjectMapper objectMapper) {
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
	}

}
