package pl.newicom.axon.coupon.rest.errors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import pl.newicom.axon.coupon.domain.CouponCommandExecutionException;

@Component
public class CouponCommandExecutionExceptionConverter implements Converter<CouponCommandExecutionException, ProblemDetail> {

	@Override
	public ProblemDetail convert(CouponCommandExecutionException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatusCode.valueOf(400), ex.getMessage());
		problemDetail.setTitle("Coupon Command Execution Failed");
		problemDetail.setProperty("couponCode", ex.getCouponCode());
		return problemDetail;
	}
}
