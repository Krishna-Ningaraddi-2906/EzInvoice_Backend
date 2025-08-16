package com.EzInvoice.backend.dto.userDto;

import lombok.Data;

@Data
public class LoginRequestDto
{
  private String email;
  private String password;
}
