package pl.newicom.axon.coupon.domain;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.newicom.axon.coupon.command.RegisterCoupon;
import pl.newicom.axon.coupon.command.UseCoupon;
import pl.newicom.axon.coupon.event.CouponRegistered;
import pl.newicom.axon.coupon.event.CouponUsed;

class CouponTest {

	private AggregateTestFixture<Coupon> fixture;

	@BeforeEach
	void setUp() {
		fixture = new AggregateTestFixture<>(Coupon.class);
	}

	@Test
	void canRegisterCoupon() {
		fixture
				.givenNoPriorActivity()
				.when(new RegisterCoupon("1234", 2, "PL"))
				.expectEvents(new CouponRegistered("1234", 2, "PL", fixture.currentTime()));
	}

	@Test
	void cannotRegisterDuplicateCouponCode() {
		fixture
				.given(new CouponRegistered("1234", 2, "PL", fixture.currentTime()))
				.when(new RegisterCoupon("1234", 3, "PL"))
				.expectNoEvents()
				.expectExceptionMessage("Coupon already registered: 1234");
	}

	@Test
	void canUseCoupon() {
		fixture
				.given(new CouponRegistered("1234", 2, "PL", fixture.currentTime()))
				.when(new UseCoupon("1234", "user-1", "PL"))
				.expectEvents(new CouponUsed("1234", "user-1"));
	}

	@Test
	void cannotUseCouponInAnotherCountry() {
		fixture
				.given(new CouponRegistered("1234", 2, "PL", fixture.currentTime()))
				.when(new UseCoupon("1234", "user-1", "DE"))
				.expectNoEvents()
				.expectExceptionMessage("Coupon 1234 is not valid in country DE");
	}

	@Test
	void cannotUseCouponMoreThanMaxUses() {
		fixture
				.given(
						new CouponRegistered("1234", 2, "PL", fixture.currentTime()),
						new CouponUsed("1234", "user-1"),
						new CouponUsed("1234", "user-2")
				)
				.when(new UseCoupon("1234", "user-3", "PL"))
				.expectNoEvents()
				.expectExceptionMessage("Coupon 1234 has no uses left");
	}

	@Test
	void userCanUseCouponOnlyOnce() {
		fixture
				.given(
						new CouponRegistered("1234", 2, "PL", fixture.currentTime()),
						new CouponUsed("1234", "user-1")
				)
				.when(new UseCoupon("1234", "user-1", "PL"))
				.expectNoEvents()
				.expectExceptionMessage("Coupon 1234 has already been used by user user-1");
	}

}
