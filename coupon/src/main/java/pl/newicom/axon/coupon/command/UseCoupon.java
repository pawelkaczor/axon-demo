package pl.newicom.axon.coupon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record UseCoupon(
		@TargetAggregateIdentifier String couponCode,
		String userId,
		String userCountryId
) {
}
