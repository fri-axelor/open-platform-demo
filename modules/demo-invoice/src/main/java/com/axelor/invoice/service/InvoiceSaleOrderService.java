package com.axelor.invoice.service;

import com.axelor.invoice.db.Invoice;
import com.axelor.sale.db.SaleOrder;
import java.util.List;

public interface InvoiceSaleOrderService {
  public Invoice createInvoiceFromSaleOrderT(SaleOrder saleOrder);

  public List<Long> invoiceLateSaleOrder();
}
