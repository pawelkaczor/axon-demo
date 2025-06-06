package pl.newicom.axon.web;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface ClientIPAddressResolver {
	String resolveClientIpAddress(HttpServletRequest request);
}
