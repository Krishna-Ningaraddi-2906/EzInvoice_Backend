package com.EzInvoice.backend.repository;

import com.EzInvoice.backend.model.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId>
{
    Optional<UserEntity> findByEmail(String email);
}
