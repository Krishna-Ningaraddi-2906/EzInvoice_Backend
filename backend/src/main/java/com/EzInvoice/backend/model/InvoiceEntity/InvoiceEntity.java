package com.EzInvoice.backend.model.InvoiceEntity;

import com.EzInvoice.backend.model.UserEntity.UserEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "invoices")
@Data
public class InvoiceEntity
{
    @Id
   private String id;
   private String customerName;
   private String companyOrIndividual;
   private String customerEmail;

   @CreatedDate
   private LocalDateTime createdAt;

   @LastModifiedDate
   private LocalDateTime updatedAt;

    @DBRef
    private UserEntity user;
}
