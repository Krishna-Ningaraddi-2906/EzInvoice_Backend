package com.EzInvoice.backend.repository;

import com.EzInvoice.backend.model.UserEntity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String>
{
    Optional<UserEntity> findByEmail(String email);
}
