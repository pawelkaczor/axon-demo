package pl.newicom.axon.countries;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class PolandCountryResolver implements ByIPCountryResolver {

	@Override
	public Optional<String> resolveCountryByIp(String ip) {
		return Optional.of("PL");
	}

}
