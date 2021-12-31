package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

	private String name;

	public MenuGroupRequest() {
	}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public MenuGroup toEntity() {
		return MenuGroup.create(name);
	}

	public String getName() {
		return name;
	}
}