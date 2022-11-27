package com.axelor.invoice.service;

import com.axelor.invoice.db.InvoiceLine;
import com.axelor.sale.db.Product;
import java.math.BigDecimal;
import java.util.Optional;

public class InvoiceLineServiceImpl implements InvoiceLineService {

  @Override
  public void setProductInformations(InvoiceLine invoiceLine) {
    Optional<Product> optionalProduct = Optional.ofNullable(invoiceLine.getProduct());
    String description = optionalProduct.map(Product::getDescription).orElse("");
    BigDecimal price = optionalProduct.map(Product::getPrice).orElse(BigDecimal.ONE);

    invoiceLine.setDescription(description);
    invoiceLine.setPrice(price);
  }

  @Override
  public void compute(InvoiceLine invoiceLine) {
    invoiceLine.setTotalPrice(invoiceLine.getPrice().multiply(invoiceLine.getQty()));
    invoiceLine.setInTaxTotalPrice(
        invoiceLine
            .getPrice()
            .multiply(invoiceLine.getQty())
            .multiply(BigDecimal.ONE.add(invoiceLine.getVat())));
  }
}
