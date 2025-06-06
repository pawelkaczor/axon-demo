package pl.newicom.axon.coupon.domain;

import java.io.Serial;

public class CouponCommandExecutionException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 3017240281403744203L;

	private final String couponCode;

	public CouponCommandExecutionException(String couponCode, String message) {
		super(message);
		this.couponCode = couponCode;
	}

	public String getCouponCode() {
		return couponCode;
	}

}
