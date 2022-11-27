package com.axelor.invoice.service;

import com.axelor.db.JPA;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.sale.db.SaleOrder;
import com.axelor.sale.db.SaleOrderLine;
import com.axelor.sale.db.repo.SaleOrderRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceSaleOrderServiceImpl implements InvoiceSaleOrderService {

  protected final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private SaleOrderRepository saleOrderRepository;

  @Inject
  public InvoiceSaleOrderServiceImpl(SaleOrderRepository saleOrderRepositor) {
    this.saleOrderRepository = saleOrderRepositor;
  }

  @Override
  @Transactional
  public Invoice createInvoiceFromSaleOrderT(SaleOrder saleOrder) {
    log.info("Creating invoice for sale order {}", saleOrder.getId());

    saleOrderRepository.copy(saleOrder, false);
    saleOrder = saleOrderRepository.find(saleOrder.getId());
    Invoice invoice = createInvoiceFromSaleOrder(saleOrder);
    saleOrder.setInvoice(invoice);
    saleOrder.setOrderStatus(SaleOrderRepository.STATUS_INVOICED);
    saleOrderRepository.save(saleOrder);

    log.info("Invoice {} created", invoice.getId());

    return invoice;
  }

  protected Invoice createInvoiceFromSaleOrder(SaleOrder saleOrder) {
    Invoice invoice = new Invoice();

    saleOrder
        .getSaleOrderLineList()
        .forEach(
            saleOrderLine ->
                invoice.addInvoiceLineListItem(createInvoiceLineFromSaleOrderLine(saleOrderLine)));

    invoice.setContact(saleOrder.getContact());
    invoice.setInvoiceDate(LocalDate.now());
    invoice.setTotalPrice(saleOrder.getTotalPrice());
    invoice.setInTaxTotalPrice(saleOrder.getInTaxTotalPrice());

    return invoice;
  }

  protected InvoiceLine createInvoiceLineFromSaleOrderLine(SaleOrderLine saleOrderLine) {
    InvoiceLine invoiceLine = new InvoiceLine();

    invoiceLine.setProduct(saleOrderLine.getProduct());
    invoiceLine.setDescription(saleOrderLine.getDescription());
    invoiceLine.setQty(saleOrderLine.getQty());
    invoiceLine.setPrice(saleOrderLine.getPrice());
    invoiceLine.setTotalPrice(saleOrderLine.getTotalPrice());
    invoiceLine.setInTaxTotalPrice(saleOrderLine.getInTaxTotalPrice());

    return invoiceLine;
  }

  @Override
  public List<Long> invoiceLateSaleOrder() {
    int i = 0;
    List<Long> invoiceIdList = new ArrayList<>();
    List<Map> saleOrderIdListMap =
        saleOrderRepository
            .all()
            .filter("self.orderStatus = :validatedStatus AND self.forecastDate < CURRENT_DATE")
            .bind("validatedStatus", SaleOrderRepository.STATUS_VALIDATED)
            .select("id")
            .fetch(0, 0);

    for (Long saleOrderId :
        saleOrderIdListMap.stream().map(m -> (Long) m.get("id")).collect(Collectors.toList())) {
      invoiceIdList.add(createInvoiceFromSaleOrderT(saleOrderRepository.find(saleOrderId)).getId());
      if (++i % 10 == 0) {
        JPA.clear();
      }
    }

    return invoiceIdList;
  }
}
