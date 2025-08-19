package com.EzInvoice.backend.controller.invoiceController;

import com.EzInvoice.backend.dto.invoiceDto.AddProductsDto;
import com.EzInvoice.backend.dto.invoiceDto.CreateInvoiceWrapperDto;
import com.EzInvoice.backend.dto.invoiceDto.GetInvoiceByCustomerDto;
import com.EzInvoice.backend.dto.invoiceDto.InvoiceResponseDto;
import com.EzInvoice.backend.service.InvoiceService.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        invoiceService.createInvoice(createInvoiceWrapperDto.getCreateInvoiceDto(),createInvoiceWrapperDto.getAddProductsDto());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-all-invoice")
    public ResponseEntity<?> getAllInvoices(Authentication authentication) // Here Authentication helps get current logged in user
    {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(invoiceService.getAllInvoicesForUser(userEmail));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-customer")
    public ResponseEntity<?> getInvoicesByCustomerEmail(@RequestBody GetInvoiceByCustomerDto dto,
                                                        Authentication authentication) {
        String loggedInUserEmail = authentication.getName();

        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesForUserByCustomerEmail(
                loggedInUserEmail, dto.getCustomerEmail());

        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No invoices found for customer: " + dto.getCustomerEmail());
        }

        return ResponseEntity.ok(invoices);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{invoiceId}")
    public ResponseEntity<?> updateInvoice(@PathVariable String invoiceId,
                                           @RequestBody CreateInvoiceWrapperDto createInvoiceWrapperDto,
                                           Authentication authentication) {
        String loggedInUserEmail = authentication.getName();

        boolean updated = invoiceService.updateInvoice(
                invoiceId,
                loggedInUserEmail,
                createInvoiceWrapperDto.getCreateInvoiceDto(),
                (List<AddProductsDto>) createInvoiceWrapperDto.getAddProductsDto()
        );

        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invoice not found or access denied");
        }

        return ResponseEntity.ok("Invoice updated successfully");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{invoiceId}")
    public ResponseEntity<?> deleteInvoice(@PathVariable String invoiceId, Authentication authentication) {
        String loggedInUserEmail = authentication.getName();
        boolean deleted = invoiceService.deleteInvoice(invoiceId, loggedInUserEmail);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invoice not found or access denied");
        }

        return ResponseEntity.ok("Invoice deleted successfully");
    }




}
