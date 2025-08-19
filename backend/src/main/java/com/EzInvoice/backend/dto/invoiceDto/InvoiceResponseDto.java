package com.EzInvoice.backend.dto.invoiceDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceResponseDto {
    private String id;
    private String customerName;
    private String companyOrIndividual;
    private String customerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
