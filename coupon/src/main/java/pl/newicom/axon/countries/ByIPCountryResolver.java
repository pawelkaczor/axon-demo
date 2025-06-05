package pl.newicom.axon.countries;

import java.util.Optional;

@FunctionalInterface
public interface ByIPCountryResolver {
	/**
	 * Resolves the country code based on the provided IP address.
	 *
	 * @param ipAddress the IP address to resolve
	 * @return the country code associated with the IP address, or None if not resolvable
	 */
	Optional<String> resolveCountryByIp(String ipAddress);
}
