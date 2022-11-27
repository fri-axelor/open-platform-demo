package com.axelor.account.service;

import com.axelor.contact.db.Contact;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.service.InvoiceSaleOrderServiceImpl;
import com.axelor.sale.db.Product;
import com.axelor.sale.db.SaleOrder;
import com.axelor.sale.db.SaleOrderLine;
import com.axelor.sale.db.repo.SaleOrderRepository;
import com.google.inject.Inject;
import java.util.Optional;

public class AccountSaleOrderServiceImpl extends InvoiceSaleOrderServiceImpl {

  @Inject
  public AccountSaleOrderServiceImpl(SaleOrderRepository saleOrderRepositor) {
    super(saleOrderRepositor);
  }

  @Override
  protected Invoice createInvoiceFromSaleOrder(SaleOrder saleOrder) {
    Invoice invoice = super.createInvoiceFromSaleOrder(saleOrder);
    Optional.ofNullable(saleOrder.getContact())
        .map(Contact::getAccount)
        .ifPresent(account -> invoice.setAccount(account));

    return invoice;
  }

  @Override
  protected InvoiceLine createInvoiceLineFromSaleOrderLine(SaleOrderLine saleOrderLine) {
    InvoiceLine invoiceLine = super.createInvoiceLineFromSaleOrderLine(saleOrderLine);
    Optional.ofNullable(saleOrderLine.getProduct())
        .map(Product::getAccount)
        .ifPresent(account -> invoiceLine.setAccount(account));

    return invoiceLine;
  }
}
