package com.axelor.invoice.service;

import com.axelor.common.ObjectUtils;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

  protected InvoiceRepository invoiceRepository;

  @Inject
  public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Override
  public void setInvoiceDate(Invoice invoice) {
    invoice.setInvoiceDate(LocalDate.now());
  }

  @Transactional
  @Override
  public Invoice validateInvoice(Invoice invoice) {
    /* on se débarasse du proxy renvoyé par le contexte
     * et on récupère l'objet correspondant managé par Hibernate
     */
    invoice = invoiceRepository.find(invoice.getId());
    invoice.setInvoiceStatus(InvoiceRepository.STATUS_VALIDATED);
    return invoiceRepository.save(invoice);
  }

  @Override
  public void compute(Invoice invoice) {
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();
    if (ObjectUtils.isEmpty(invoiceLineList)) {
      return;
    }

    invoice.setTotalPrice(
        invoiceLineList.stream()
            .map(InvoiceLine::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
    invoice.setInTaxTotalPrice(
        invoiceLineList.stream()
            .map(InvoiceLine::getInTaxTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

    BigDecimal totalPrice = BigDecimal.ZERO;
    BigDecimal inTaxTotalPrice = BigDecimal.ZERO;
    for (InvoiceLine line : invoiceLineList) {
      totalPrice = totalPrice.add(line.getTotalPrice());
      inTaxTotalPrice = inTaxTotalPrice.add(line.getInTaxTotalPrice());
    }

    invoice.setTotalPrice(totalPrice);
    invoice.setInTaxTotalPrice(inTaxTotalPrice);
  }
}
