package pl.newicom.axon.coupon.rest;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import pl.newicom.axon.countries.ByIPCountryResolver;
import pl.newicom.axon.coupon.command.RegisterCoupon;
import pl.newicom.axon.coupon.command.UseCoupon;
import pl.newicom.axon.web.ClientIPAddressResolver;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class CouponController {
	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;
	private final ByIPCountryResolver countryResolver;
	private final ClientIPAddressResolver clientIPAddressResolver;

	public CouponController(
			CommandGateway commandGateway,
			QueryGateway queryGateway,
			ByIPCountryResolver countryResolver,
			ClientIPAddressResolver clientIPAddressResolver) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
		this.countryResolver = countryResolver;
		this.clientIPAddressResolver = clientIPAddressResolver;
	}

	@PostMapping("/coupons")
	public CompletableFuture<Void> registerCoupon(
			@RequestParam("couponCode") String couponCode,
			@RequestParam("countryId") String countryId,
			@RequestParam("maxUses") Integer maxUses) {

		RegisterCoupon command =
				new RegisterCoupon(
						couponCode.toLowerCase(),
						maxUses,
						countryId);

		return commandGateway.send(command);
	}

	@PostMapping("/useCoupon")
	public CompletableFuture<Void> useCoupon(
			@RequestParam("couponCode") String couponCode) {

		String userId = UUID.randomUUID().toString();

		return resolveClientCountry()
				.map(countryId -> commandGateway.<Void>send(new UseCoupon(couponCode, userId, countryId)))
				.orElseGet(() -> CompletableFuture.failedFuture(new RuntimeException("Unable to resolve client country")));
	}

	private Optional<String> resolveClientCountry() {
		return clientIPAddressResolver.resolveClientIpAddress()
				.flatMap(countryResolver::resolveCountryByIp);
	}

}

