package com.axelor.invoice.web;

import com.axelor.inject.Beans;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineController {
  public void setProductInformations(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);

    Beans.get(InvoiceLineService.class).setProductInformations(invoiceLine);
    response.setValues(invoiceLine); // valeur renvoyée à la vue
  }

  public void compute(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);

    Beans.get(InvoiceLineService.class).compute(invoiceLine);
    response.setValues(invoiceLine); // valeur renvoyée à la vue
  }
}
