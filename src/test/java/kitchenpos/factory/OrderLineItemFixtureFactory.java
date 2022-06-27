package kitchenpos.factory;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    public static OrderLineItem createOrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem createOrderLineItem(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
