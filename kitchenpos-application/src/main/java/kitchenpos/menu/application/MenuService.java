package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@Service
@Transactional
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuGroupService menuGroupService;
	private final ProductService productService;
	private final MenuValidator menuValidator;

	public MenuService(final MenuRepository menuRepository,
		final MenuGroupService menuGroupService,
		final ProductService productService,
		final MenuValidator menuValidator) {

		this.menuRepository = menuRepository;
		this.menuGroupService = menuGroupService;
		this.productService = productService;
		this.menuValidator = menuValidator;
	}

	public MenuResponse create(final MenuRequest request) {
		MenuGroup menuGroup = menuGroupService.getById(request.getMenuGroupId());
		Menu menu = request.toEntity(menuGroup);
		List<MenuProduct> menuProductList = getMenuProductList(request.getMenuProducts());
		menu.addMenuProducts(menuProductList);
		menuValidator.isOverPrice(menu);
		return new MenuResponse(menuRepository.save(menu));
	}

	private List<MenuProduct> getMenuProductList(List<MenuProductRequest> menuProductRequests) {
		return menuProductRequests.stream().map(this::getMenuProduct)
			.collect(Collectors.toList());
	}

	private MenuProduct getMenuProduct(MenuProductRequest request) {
		Product product = productService.getById(request.getProductId());
		return MenuProduct.create(product.getId(), request.getQuantity());
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		List<Menu> menuList = menuRepository.findAll();
		return menuList.stream().map(MenuResponse::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Menu getById(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다"));
	}
}