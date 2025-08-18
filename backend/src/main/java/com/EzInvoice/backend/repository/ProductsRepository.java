package com.EzInvoice.backend.repository;

import com.EzInvoice.backend.model.InvoiceEntity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductsRepository extends MongoRepository<ProductEntity, String>
{
    List<ProductEntity> findByInvoiceId(String invoiceId);
}
