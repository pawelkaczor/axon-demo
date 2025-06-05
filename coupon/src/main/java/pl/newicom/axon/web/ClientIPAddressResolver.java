package pl.newicom.axon.web;

import java.util.Optional;

@FunctionalInterface
public interface ClientIPAddressResolver {
	Optional<String> resolveClientIpAddress();
}
