package com.EzInvoice.backend.model.InvoiceEntity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
public class ProductEntity
{
    @Id
    private String id;
    private String invoiceId;
    private String productName;
    private String price;
    private String quantity;
}
