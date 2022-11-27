package com.axelor.account.module;

import com.axelor.account.service.AccountInvoiceLineServiceImpl;
import com.axelor.account.service.AccountInvoiceService;
import com.axelor.account.service.AccountInvoiceServiceImpl;
import com.axelor.account.service.AccountSaleOrderServiceImpl;
import com.axelor.app.AxelorModule;
import com.axelor.invoice.service.InvoiceLineServiceImpl;
import com.axelor.invoice.service.InvoiceSaleOrderServiceImpl;
import com.axelor.invoice.service.InvoiceServiceImpl;

public class AccountModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(AccountInvoiceService.class).to(AccountInvoiceServiceImpl.class);
    bind(InvoiceServiceImpl.class).to(AccountInvoiceServiceImpl.class);
    bind(InvoiceLineServiceImpl.class).to(AccountInvoiceLineServiceImpl.class);
    bind(InvoiceSaleOrderServiceImpl.class).to(AccountSaleOrderServiceImpl.class);
  }
}
