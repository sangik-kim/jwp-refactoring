package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderService orderService, final OrderTableRepository orderTableRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.from(
                orderTableRepository.save(orderTableRequest.toOrderTable())
        );
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.fromList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = this.findOrderTable(orderTableId);
        validateChangeEmpty(orderTable);

        orderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        int numberOfGuests = orderTableRequest.getNumberOfGuests();
        OrderTable orderTable = this.findOrderTable(orderTableId);
        validateChangeNumberOfGuests(orderTable);

        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTable);
    }

    private void validateChangeNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateChangeEmpty(OrderTable orderTable) {
        validateExistTableGroup(orderTable);
        validateOrderTableStatus(orderTable);
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        if (orderService.isExistDontUnGroupState(orderTable)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistTableGroup(OrderTable orderTable) {
        if (orderTable.getTableGroup() != null) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }
}
