package kitchenpos.product;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends BaseContollerTest {

    @Test
    @DisplayName("새로운 상품을 등록합니다.")
    void createProduct() throws Exception {
        상품_등록_요청(ProductTestSupport.createProduct("맛있는치킨", BigDecimal.valueOf(20000))
                , status().isCreated());
    }

    @Test
    @DisplayName("새로운 상품 등록 시 이름이 없으면 오류 발생")
    void createProductNoNameOccurredException() {
        Product product = new Product();
        product.setName(null);
        product.setPrice(BigDecimal.valueOf(20000));

        assertThatThrownBy(() -> {
            상품_등록_요청(product, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("NULL not allowed for column \"NAME\"");
    }

    @Test
    @DisplayName("새로운 상품 등록 시 가격이 없으면 오류 발생")
    void createProductNoPriceOccurredException() {
        Product product = new Product();
        product.setName("맛있는치킨");
        product.setPrice(null);

        assertThatThrownBy(() -> {
            상품_등록_요청(product, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
    }

    private void 상품_등록_요청(Product product, ResultMatcher status) throws Exception {
        this.mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product))
                )
                .andDo(print())
                .andExpect(status)
        ;
    }

    @Test
    @DisplayName("모든 상품 목록을 조회합니다.")
    void getProducts() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").exists())
                .andReturn();

        String responseProducts = mvcResult.getResponse().getContentAsString();
        ArrayList<Product> products
                = this.objectMapper.readValue(responseProducts, new TypeReference<ArrayList<Product>>() {});

        assertThat(products).hasSize(6);
    }
}