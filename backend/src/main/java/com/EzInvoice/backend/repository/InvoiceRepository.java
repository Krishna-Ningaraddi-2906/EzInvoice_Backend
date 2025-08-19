package com.EzInvoice.backend.repository;

import com.EzInvoice.backend.model.InvoiceEntity.InvoiceEntity;
import com.EzInvoice.backend.model.UserEntity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvoiceRepository extends MongoRepository<InvoiceEntity, String>
{
    List<InvoiceEntity> findByUser(UserEntity user);
    List<InvoiceEntity> findByUserAndCustomerEmail(UserEntity user, String customerEmail);
}
