package com.EzInvoice.backend.dto.invoiceDto;

import lombok.Data;

@Data
public class CreateInvoiceDto
{
    private String customerName;
    private String customerEmail;
    private String companyOrIndividual;
}
