package pl.newicom.axon.countries;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
// TODO: Consider caching results to avoid repeated lookups for the same IP address
public interface ByIPCountryResolver {
	/**
	 * Resolves the country code based on the provided IP address.
	 *
	 * @param ipAddress the IP address to resolve
	 * @return the country code associated with the IP address
	 */
	CompletableFuture<String> resolveCountryByIp(String ipAddress);
}
