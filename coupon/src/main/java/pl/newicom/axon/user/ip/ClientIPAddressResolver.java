package pl.newicom.axon.user.ip;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface ClientIPAddressResolver {
	String resolveClientIpAddress(HttpServletRequest request);
}
