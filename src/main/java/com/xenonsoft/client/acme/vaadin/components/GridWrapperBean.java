package com.xenonsoft.client.acme.vaadin.components;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class GridWrapperBean {

	private List<GridBean> lines = new ArrayList<>();
	private String errorMesage = null;
	private BigDecimal total = null;
}