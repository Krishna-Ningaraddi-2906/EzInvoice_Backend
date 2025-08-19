package com.EzInvoice.backend.dto.invoiceDto;

import lombok.Data;

@Data
public class CreateInvoiceWrapperDto
{
    private CreateInvoiceDto createInvoiceDto;
    private AddProductsDto addProductsDto;
}
