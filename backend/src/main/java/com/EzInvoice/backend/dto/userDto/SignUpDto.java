package com.EzInvoice.backend.dto.userDto;
import lombok.Data;

@Data
public class SignUpDto
{
    private String userName;
    private String password;
    private String email;
    private String contactNo;
    private String companyName;
    private byte[] logo;
}
