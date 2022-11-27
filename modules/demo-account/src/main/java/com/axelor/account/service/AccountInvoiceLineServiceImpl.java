package com.axelor.account.service;

import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.service.InvoiceLineServiceImpl;
import com.axelor.sale.db.Product;
import java.util.Optional;

public class AccountInvoiceLineServiceImpl extends InvoiceLineServiceImpl {

  @Override
  public void setProductInformations(InvoiceLine invoiceLine) {
    super.setProductInformations(invoiceLine);

    invoiceLine.setAccount(
        Optional.ofNullable(invoiceLine.getProduct()).map(Product::getAccount).orElse(null));
  }
}
