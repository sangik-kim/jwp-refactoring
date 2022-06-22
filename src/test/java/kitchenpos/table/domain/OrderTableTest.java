package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 Domain 단위 테스트")
class OrderTableTest {

    @DisplayName("빈 테이블 여부 변경이 가능한지 확인한다.")
    @Test
    void checkPossibleChangeEmpty() {

        //given
        OrderTable orderTable1 = new OrderTable(null, 3, false);
        orderTable1.addOrder(new Order(null, OrderStatus.MEAL, null));
        orderTable1.addOrder(new Order(null, OrderStatus.COMPLETION, null));
        orderTable1.addOrder(new Order(null, OrderStatus.COOKING, null));

        OrderTable orderTable2 = new OrderTable(null, 3, false);
        orderTable2.addOrder(new Order(null, OrderStatus.COMPLETION, null));
        orderTable2.addOrder(new Order(null, OrderStatus.COMPLETION, null));
        orderTable2.addOrder(new Order(null, OrderStatus.COMPLETION, null));

        //when then
        assertThatIllegalStateException()
                .isThrownBy(orderTable1::checkPossibleChangeEmpty);
        assertThatNoException()
                .isThrownBy(orderTable2::checkPossibleChangeEmpty);

    }
}
