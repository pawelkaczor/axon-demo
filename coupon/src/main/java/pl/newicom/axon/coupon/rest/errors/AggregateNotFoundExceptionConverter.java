package pl.newicom.axon.coupon.rest.errors;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
// TODO: Axon's AggregateNotFoundException does not contain aggregate class name, so we cannot use it to determine the type of aggregate.
//       We should consider creating a custom exception that extends AggregateNotFoundException and includes the aggregate class name.
//       For now, since there is only single Coupon AR, we can use the class name directly in the converter.
public class AggregateNotFoundExceptionConverter implements Converter<AggregateNotFoundException, ProblemDetail> {
	@Override
	public ProblemDetail convert(AggregateNotFoundException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), "Aggregate not found");
		problemDetail.setTitle("Coupon Not Found");
		problemDetail.setProperty("couponCode", ex.getAggregateIdentifier());
		return problemDetail;

	}
}
