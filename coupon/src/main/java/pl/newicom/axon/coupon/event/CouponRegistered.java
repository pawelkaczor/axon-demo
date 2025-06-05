package pl.newicom.axon.coupon.event;

import java.time.Instant;

public record CouponRegistered(
		String couponCode,
		Integer maxUses,
		String countryId,
		Instant registeredAt
) {
}
