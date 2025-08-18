package com.EzInvoice.backend.repository;

import com.EzInvoice.backend.model.InvoiceEntity.InvoiceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface InvoiceRepository extends MongoRepository<InvoiceEntity, String>
{
}
