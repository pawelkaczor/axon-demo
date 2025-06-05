package pl.newicom.axon.web;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class DefaultClientIPAddressResolver implements ClientIPAddressResolver {
	@Override
	public Optional<String> resolveClientIpAddress() {
		// TODO: Implement logic to resolve the client IP address.
		return Optional.empty();
	}
}
