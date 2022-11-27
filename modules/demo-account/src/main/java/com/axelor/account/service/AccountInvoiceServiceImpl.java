package com.axelor.account.service;

import com.axelor.account.db.Account;
import com.axelor.account.db.Move;
import com.axelor.account.db.MoveLine;
import com.axelor.account.db.repo.MoveLineRepository;
import com.axelor.common.ObjectUtils;
import com.axelor.contact.db.Contact;
import com.axelor.i18n.I18n;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.invoice.service.InvoiceServiceImpl;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountInvoiceServiceImpl extends InvoiceServiceImpl implements AccountInvoiceService {

  @Inject
  public AccountInvoiceServiceImpl(InvoiceRepository invoiceRepository) {
    super(invoiceRepository);
  }

  public void setContactInformations(Invoice invoice) {
    invoice.setAccount(
        Optional.ofNullable(invoice.getContact()).map(Contact::getAccount).orElse(null));
  }

  @Override
  @Transactional
  public Move createMoveFromInvoiceT(Invoice invoice) {
    checkFields(invoice);

    invoice = super.invoiceRepository.find(invoice.getId());
    Move move = createMoveFromInvoice(invoice);

    invoice.setMove(move);

    super.invoiceRepository.save(invoice);

    return move;
  }

  private void checkFields(Invoice invoice) {
    if (invoice.getContact() == null) {
      throw new IllegalStateException(
          String.format(
              I18n.get("Invoice %d must have a contact in order to be ventilated"),
              invoice.getId()));
    }

    if (invoice.getAccount() == null) {
      throw new IllegalStateException(
          String.format(
              I18n.get("Invoice %d must have an account in order to be ventilated"),
              invoice.getId()));
    }

    List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();
    if (ObjectUtils.isEmpty(invoiceLineList)) {
      throw new IllegalStateException(
          String.format(
              I18n.get("Please fill the invoice lines before ventilating the invoice %d"),
              invoice.getId()));
    }
    if (invoiceLineList.stream().anyMatch(invoiceLine -> invoiceLine.getAccount() == null)) {
      throw new IllegalStateException(
          String.format(
              I18n.get(
                  "All invoice lines of invoice %d must have an account in order to be ventilated"),
              invoice.getId()));
    }
  }

  protected Move createMoveFromInvoice(Invoice invoice) {
    Move move = new Move();

    move.setInvoice(invoice);

    invoice
        .getInvoiceLineList()
        .forEach(
            invoiceLine ->
                move.addMoveLineListItem(
                    createMoveLine(
                        invoiceLine.getInTaxTotalPrice(),
                        invoiceLine.getAccount(),
                        MoveLineRepository.TYPE_CREDIT)));

    move.addMoveLineListItem(
        createMoveLine(
            move.getMoveLineList().stream()
                .map(MoveLine::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add),
            invoice.getAccount(),
            MoveLineRepository.TYPE_DEBIT));

    move.setOperationDate(invoice.getInvoiceDate());

    return move;
  }

  protected MoveLine createMoveLine(BigDecimal amount, Account account, int type) {
    MoveLine moveLine = new MoveLine();

    switch (type) {
      case MoveLineRepository.TYPE_DEBIT:
        moveLine.setDebitAmount(amount);
        break;
      case MoveLineRepository.TYPE_CREDIT:
        moveLine.setCreditAmount(amount);
        break;
      default:
        throw new IllegalArgumentException(
            String.format(I18n.get("MoveLine type %d unknown"), type));
    }

    moveLine.setType(type);
    moveLine.setAccount(account);

    return moveLine;
  }
}
