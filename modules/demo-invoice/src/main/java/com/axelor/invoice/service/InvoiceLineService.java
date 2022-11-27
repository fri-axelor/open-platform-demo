package com.axelor.invoice.service;

import com.axelor.invoice.db.InvoiceLine;

public interface InvoiceLineService {
  void setProductInformations(InvoiceLine invoiceLine);

  void compute(InvoiceLine invoiceLine);
}
