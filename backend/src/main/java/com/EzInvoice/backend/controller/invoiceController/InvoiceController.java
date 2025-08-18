package com.EzInvoice.backend.controller.invoiceController;

import com.EzInvoice.backend.dto.invoiceDto.CreateInvoiceWrapperDto;
import com.EzInvoice.backend.dto.invoiceDto.InvoiceDto;
import com.EzInvoice.backend.dto.invoiceDto.ProductsDto;
import com.EzInvoice.backend.service.InvoiceService.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("invoice")
public class InvoiceController
{

    @Autowired
    private InvoiceService invoiceService;

    @PreAuthorize("isAuthenticated()") //Only allow access if the user is logged in / authenticated
    @PostMapping("/create-invoice")
    public ResponseEntity<?> createInvoice(@RequestBody CreateInvoiceWrapperDto createInvoiceWrapperDto)
    {
        invoiceService.createInvoice(createInvoiceWrapperDto.getInvoiceDto(),createInvoiceWrapperDto.getProductsDto());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
