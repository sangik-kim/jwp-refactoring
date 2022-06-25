package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블 객체가 같은지 검증")
    void verifyEqualsOrderTable() {
        final OrderTableV2 orderTable = new OrderTableV2(1L, new TableGroupV2(), 5, false);

        assertThat(orderTable).isEqualTo(new OrderTableV2(1L, new TableGroupV2(), 5, false));
    }
}
