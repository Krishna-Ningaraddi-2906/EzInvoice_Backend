package com.EzInvoice.backend.dto.invoiceDto;

import lombok.Data;

@Data
public class CreateInvoiceWrapperDto
{
    private InvoiceDto invoiceDto;
    private ProductsDto productsDto;
}
