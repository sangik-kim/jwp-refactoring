package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableGroupRestAssured.단체지정_등록_요청;
import static kitchenpos.acceptance.TableGroupRestAssured.단체지정_해제_요청;
import static kitchenpos.acceptance.TableRestAssured.주문테이블_등록_요청;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체지정 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private TableGroup 단체지정;

    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTable 치킨주문테이블 = 주문테이블_등록_요청(createOrderTable(null, null, 2, true)).as(OrderTable.class);
        OrderTable 피자주문테이블 = 주문테이블_등록_요청(createOrderTable(null, null, 3, true)).as(OrderTable.class);
        단체지정 = createTableGroup(1L, null, Lists.newArrayList(치킨주문테이블, 피자주문테이블));
    }

    /**
     * When 단체지정을 등록 요청하면
     * Then 단체지정이 등록 됨
     */
    @DisplayName("단체지정을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 단체지정_등록_요청(단체지정);

        // then
        단체지정_등록됨(response);
    }

    /**
     * Given 단체지정을 등록하고
     * When 단체지정을 해제 요청하면
     * Then 단체지정이 해제 됨
     */
    @DisplayName("단체지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup 등록한_단체지정 = 단체지정_등록_요청(단체지정).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체지정_해제_요청(등록한_단체지정.getId());

        // then
        단체지정_해제됨(response);
    }

    private void 단체지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
