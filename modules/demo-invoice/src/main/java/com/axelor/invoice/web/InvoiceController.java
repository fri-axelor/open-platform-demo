package com.axelor.invoice.web;

import com.axelor.inject.Beans;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {

  public void setInvoiceDate(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    request.getContext().get("toto");

    Beans.get(InvoiceService.class).setInvoiceDate(invoice);
    response.setValue("invoiceDate", invoice.getInvoiceDate()); // valeur renvoyée à la vue
    String.format("Bonjour %s, j'ai %d ans", "Fabien", 27);
  }

  public void validateInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);

    Beans.get(InvoiceService.class).validateInvoice(invoice);
    response.setReload(
        true); // valeur enregistrée en base. Le serveur indique à la vue de se rafraichir
  }

  public void compute(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);

    Beans.get(InvoiceService.class).compute(invoice);
    response.setValue("totalPrice", invoice.getTotalPrice());
    response.setValue("inTaxTotalPrice", invoice.getInTaxTotalPrice());
  }
}
