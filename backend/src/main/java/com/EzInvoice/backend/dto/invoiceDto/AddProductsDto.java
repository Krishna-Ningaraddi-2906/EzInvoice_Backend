package com.EzInvoice.backend.dto.invoiceDto;

import lombok.Data;

@Data
public class AddProductsDto {

    private String productName;
    private String price;
    private String quantity;
}
