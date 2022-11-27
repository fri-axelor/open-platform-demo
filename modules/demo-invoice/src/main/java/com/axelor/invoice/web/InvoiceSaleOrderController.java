package com.axelor.invoice.web;

import com.axelor.common.ObjectUtils;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.service.InvoiceSaleOrderService;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.sale.db.SaleOrder;
import java.util.List;

public class InvoiceSaleOrderController {

  public void createInvoiceFromSaleOrder(ActionRequest request, ActionResponse response) {
    SaleOrder saleOrder = request.getContext().asType(SaleOrder.class);
    Invoice invoice =
        Beans.get(InvoiceSaleOrderService.class).createInvoiceFromSaleOrderT(saleOrder);

    response.setReload(true);
    response.setView(
        ActionView.define(I18n.get("Invoice"))
            .model(Invoice.class.getName())
            .add("form", "invoice-form")
            .context("_showRecord", invoice.getId())
            .map());
  }

  public void invoiceLateSaleOrder(ActionRequest request, ActionResponse response) {
    List<Long> invoiceIdList = Beans.get(InvoiceSaleOrderService.class).invoiceLateSaleOrder();

    if (ObjectUtils.isEmpty(invoiceIdList)) {
      response.setNotify(I18n.get("There's no sale orders to invoice"));
    } else {
      response.setView(
          ActionView.define(I18n.get("Invoices"))
              .model(Invoice.class.getName())
              .add("grid", "invoice-grid")
              .add("form", "invoice-form")
              .domain("self.id in :idList")
              .context("idList", invoiceIdList)
              .map());
    }
  }
}
