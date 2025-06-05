package pl.newicom.axon.coupon.domain;

public class CouponCommandExecutionException extends RuntimeException {

	private final String couponCode;

	public CouponCommandExecutionException(String couponCode, String message) {
		super(message);
		this.couponCode = couponCode;
	}

	public String getCouponCode() {
		return couponCode;
	}

}
