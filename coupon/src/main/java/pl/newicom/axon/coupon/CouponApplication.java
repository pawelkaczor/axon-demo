package pl.newicom.axon.coupon;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.axonframework.modelling.saga.repository.jpa.SagaEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.newicom.axon.user.country.ByIPCountryResolver;
import pl.newicom.axon.user.country.SimpleCountryResolver;
import pl.newicom.axon.http.AxonCommandMessageInterceptor;
import pl.newicom.axon.user.ip.ClientIPAddressResolver;
import pl.newicom.axon.user.ip.DefaultClientIPAddressResolver;

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
		return new SimpleCountryResolver(new HashMap<>(), "PL");
	}

	@Bean
	public ClientIPAddressResolver clientIPAddressResolver() {
		return new DefaultClientIPAddressResolver();
	}

	@Bean
	public CommandGateway commandGateway(CommandBus commandBus, AxonCommandMessageInterceptor commandMessageInterceptor) {
		//noinspection resource
		commandBus.registerHandlerInterceptor(commandMessageInterceptor);
		return DefaultCommandGateway.builder().commandBus(commandBus).build();

	}


	@Autowired
	public void configureSerializers(ObjectMapper objectMapper) {
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
	}

}
