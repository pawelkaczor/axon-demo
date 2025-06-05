package pl.newicom.axon.coupon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RegisterCoupon(
		@TargetAggregateIdentifier String couponCode,
		Integer maxUses,
		String countryId) {
}
