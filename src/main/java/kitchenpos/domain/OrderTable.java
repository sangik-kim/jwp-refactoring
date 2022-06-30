package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.EAGER)
    private final List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public void setTableGroupId(final Long tableGroupId) {
    }

    public void includeInGroup(final TableGroup tableGroup) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("빈 테이블이 아니면 단체 지정할 수 없습니다.");
        }
        if (isGrouped()) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블 입니다.");
        }
        this.tableGroup = tableGroup;
    }

    private boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    public void excludeFromGroup() {
        if (orders.stream().anyMatch(order -> isOrderStatusInCookingOrMeal(order.getOrderStatus()))) {
            throw new IllegalArgumentException("조리 또는 식사인 주문이 존재하는 테이블은 단체 지정 취소할 수 없습니다.");
        }
        this.tableGroup = null;
    }

    private boolean isOrderStatusInCookingOrMeal(OrderStatus orderStatus) {
        return orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수가 0명 보다 작을 수는 없습니다.");
        }
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 방문한 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 주문 등록 가능 상태를 변경할 수 없습니다.");
        }
        if (orders.stream().anyMatch(order -> isOrderStatusInCookingOrMeal(order.getOrderStatus()))) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 테이블은 주문 등록 가능 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
        if (order.getOrderTable() != this) {
            order.changeOrderTable(this);
        }
    }

    public boolean contains(Order order) {
        return orders.contains(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests
                && empty == that.empty
                && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                ", orders=" + orders +
                '}';
    }
}
