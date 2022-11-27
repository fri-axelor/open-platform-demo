package com.axelor.account.service;

import com.axelor.account.db.Move;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.service.InvoiceService;

public interface AccountInvoiceService extends InvoiceService {

  void setContactInformations(Invoice invoice);

  Move createMoveFromInvoiceT(Invoice invoice);
}
