package com.EzInvoice.backend.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserEntity
{
    @Id
    private ObjectId id;
    private String userName;
    private String password;
    private String email;
    private String contactNo;
    private String companyName;
    private byte[] logo;
}
