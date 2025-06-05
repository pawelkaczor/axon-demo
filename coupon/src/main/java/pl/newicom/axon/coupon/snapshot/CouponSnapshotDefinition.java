package pl.newicom.axon.coupon.snapshot;

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.stereotype.Component;

@Component("couponSnapshotDefinition")
public class CouponSnapshotDefinition extends EventCountSnapshotTriggerDefinition {

    public CouponSnapshotDefinition(Snapshotter snapshotter) {
        super(snapshotter, 10);
    }
}
