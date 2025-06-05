package pl.newicom.axon.coupon.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;
import pl.newicom.axon.coupon.command.RegisterCoupon;
import pl.newicom.axon.coupon.command.UseCoupon;
import pl.newicom.axon.coupon.event.CouponRegistered;
import pl.newicom.axon.coupon.event.CouponUsed;

@Aggregate(snapshotTriggerDefinition = "couponSnapshotDefinition")
public class Coupon {

	@AggregateIdentifier
	private String couponCode;
	private Integer usesLeft;
	private Set<String> usedBy = new HashSet<>();
	private String countryId;

	public Coupon() {
	}

	@JsonCreator
	public Coupon(@JsonProperty("couponCode") String couponCode,
				  @JsonProperty("usesLeft") Integer usesLeft,
				  @JsonProperty("countryId") String countryId) {
		this.couponCode = couponCode;
		this.usesLeft = usesLeft;
		this.countryId = countryId;
	}

	@CommandHandler
	@CreationPolicy(value = AggregateCreationPolicy.CREATE_IF_MISSING)
	public void handle(RegisterCoupon command) {
		if (this.couponCode == null) // couponCode is null for newly created aggregate {
			apply(new CouponRegistered(command.couponCode(), command.maxUses(), command.countryId(), GenericEventMessage.clock.instant()));
		else {
			throw new CouponCommandExecutionException(couponCode, "Coupon already registered: " + couponCode);
		}
	}

	@CommandHandler
	public void handle(UseCoupon command) {
		if (!this.countryId.equals(command.userCountryId())) {
			throw new CouponCommandExecutionException(couponCode, String.format("Coupon %s is not valid in country %s", couponCode, command.userCountryId()));
		} else if (this.usesLeft <= 0) {
			throw new CouponCommandExecutionException(couponCode, String.format("Coupon %s has no uses left", couponCode));
		} else if (this.usedBy.contains(command.userId())) {
			throw new CouponCommandExecutionException(couponCode, String.format("Coupon %s has already been used by user %s", couponCode, command.userId()));
		} else {
			apply(new CouponUsed(command.couponCode(), command.userId()));
		}
	}

	@EventSourcingHandler
	protected void handle(CouponRegistered event) {
		this.couponCode = event.couponCode();
		this.usesLeft = event.maxUses();
		this.countryId = event.countryId();
	}

	@EventSourcingHandler
	protected void handle(CouponUsed event) {
		this.usesLeft = this.usesLeft - 1;
		this.usedBy.add(event.userId());
	}

	// getters for Jackson / JSON Serialization

	@SuppressWarnings("unused")
	public String getCouponCode() {
		return couponCode;
	}

	@SuppressWarnings("unused")
	public Integer getUsesLeft() {
		return usesLeft;
	}

	@SuppressWarnings("unused")
	public String getCountryId() {
		return countryId;
	}

}
