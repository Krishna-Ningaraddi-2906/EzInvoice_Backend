package com.EzInvoice.backend.model.UserEntity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserEntity
{
    @Id
    private String id;
    private String userName;
    private String password;
    private String email;
    private String contactNo;
    private String companyName;
    private byte[] logo;
}
