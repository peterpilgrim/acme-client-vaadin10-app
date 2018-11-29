package com.xenonsoft.client.acme.vaadin.components;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GridBean {

	public static final String column_supplier = "supplier";
	public static final String column_invoiceRef = "invoiceRef";
	public static final String column_orderRef = "orderRef";
	public static final String column_amount = "amount";
	public static final String column_paymentDate = "paymentDate";
	public static final String column_issue = "issue";

	private boolean hasError;
	private String supplier;
	private String invoiceRef;
	private String orderRef;
	private BigDecimal amount;
	private LocalDate paymentDate;
	private String issue;

	public boolean hasError() {
		return hasError;
	}

}