package com.axelor.invoice.module;

import com.axelor.app.AxelorModule;
import com.axelor.invoice.db.InvoiceManagementRepository;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.invoice.service.InvoiceLineService;
import com.axelor.invoice.service.InvoiceLineServiceImpl;
import com.axelor.invoice.service.InvoiceSaleOrderService;
import com.axelor.invoice.service.InvoiceSaleOrderServiceImpl;
import com.axelor.invoice.service.InvoiceService;
import com.axelor.invoice.service.InvoiceServiceImpl;

public class InvoiceModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(InvoiceService.class).to(InvoiceServiceImpl.class);
    bind(InvoiceRepository.class).to(InvoiceManagementRepository.class);
    bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
    bind(InvoiceSaleOrderService.class).to(InvoiceSaleOrderServiceImpl.class);
  }
}
