package com.axelor.invoice.service;

import com.axelor.invoice.db.Invoice;

public interface InvoiceService {

  void setInvoiceDate(Invoice invoice);

  Invoice validateInvoice(Invoice invoice);

  void compute(Invoice invoice);
}
