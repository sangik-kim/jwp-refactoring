package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroup create(final List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableValidator.findExistsOrderTableByIdIn(orderTableIds);
        return tableGroupRepository.save(TableGroup.group(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findByIdWithOrderTable(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        List<Long> orderTableIds = tableGroup.getOrderTables().getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderTableValidator.checkOrderStatusIn(orderTableIds);

        tableGroup.ungroup();
    }
}
