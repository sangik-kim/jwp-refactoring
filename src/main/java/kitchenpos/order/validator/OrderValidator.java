package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;

public interface OrderValidator {
    void validate(Order order);
}
