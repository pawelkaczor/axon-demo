package pl.newicom.axon.user.country;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

@Component
public class SimpleCountryResolver implements ByIPCountryResolver {

	private final Map<String, String> ipToCountryMap;
	private final Optional<String> defaultCountry;

	public SimpleCountryResolver(Map<String, String> ipToCountryMap) {
		this.ipToCountryMap = ipToCountryMap;
		this.defaultCountry = Optional.empty();
	}

	public SimpleCountryResolver(Map<String, String> ipToCountryMap, String defaultCountry) {
		this.ipToCountryMap = ipToCountryMap;
		this.defaultCountry = Optional.ofNullable(defaultCountry);
	}

	@Override
	public CompletableFuture<String> resolveCountryByIp(String ip) {
		String country = ipToCountryMap.get(ip);
		if (country == null) {
			//noinspection OptionalIsPresent
			if (defaultCountry.isPresent()) {
				return CompletableFuture.completedFuture(defaultCountry.get());
			} else {
				return CompletableFuture.failedFuture(new RuntimeException("Country not found for IP: " + ip));
			}
		} else {
			return CompletableFuture.completedFuture(country);
		}
	}

}
