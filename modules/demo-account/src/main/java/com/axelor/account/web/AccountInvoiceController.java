package com.axelor.account.web;

import com.axelor.account.db.Move;
import com.axelor.account.service.AccountInvoiceService;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.invoice.db.Invoice;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class AccountInvoiceController {

  public void setContactInformations(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Beans.get(AccountInvoiceService.class).setContactInformations(invoice);
    response.setValues(invoice);
  }

  public void createMoveFromInvoice(ActionRequest request, ActionResponse response) {
    try {
      Invoice invoice = request.getContext().asType(Invoice.class);
      Move move = Beans.get(AccountInvoiceService.class).createMoveFromInvoiceT(invoice);

      response.setReload(true);
      response.setView(
          ActionView.define(I18n.get("Move"))
              .model(Move.class.getName())
              .add("form", "move-form")
              .context("_showRecord", move.getId())
              .map());
    } catch (Exception e) {
      response.setError(e.getMessage());
    }
  }
}
