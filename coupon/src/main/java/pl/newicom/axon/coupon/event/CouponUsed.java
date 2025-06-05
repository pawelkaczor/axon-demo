package pl.newicom.axon.coupon.event;

public record CouponUsed(
				String couponCode,
				String userId
) {
}
