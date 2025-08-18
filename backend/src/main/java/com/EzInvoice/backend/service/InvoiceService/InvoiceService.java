package com.EzInvoice.backend.service.InvoiceService;

import com.EzInvoice.backend.dto.invoiceDto.InvoiceDto;
import com.EzInvoice.backend.dto.invoiceDto.ProductsDto;
import com.EzInvoice.backend.model.InvoiceEntity.InvoiceEntity;
import com.EzInvoice.backend.model.InvoiceEntity.ProductEntity;
import com.EzInvoice.backend.model.UserEntity.UserEntity;
import com.EzInvoice.backend.repository.InvoiceRepository;
import com.EzInvoice.backend.repository.ProductsRepository;
import com.EzInvoice.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UserRepository userRepository;

    public void createInvoice(InvoiceDto invoiceDto, ProductsDto productsDto) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setCustomerName(invoiceDto.getCustomerName());
        invoiceEntity.setCustomerEmail(invoiceDto.getCustomerEmail());
        invoiceEntity.setCompanyOrIndividual(invoiceDto.getCompanyOrIndividual());

        // Get logged-in user, this is used to get the current user logged in and helps in mapping the invoices to user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            invoiceEntity.setUser(user); // <-- link invoice to user
        }

        InvoiceEntity savedInvoice = invoiceRepository.save(invoiceEntity);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productsDto.getProductName());
        productEntity.setQuantity(productsDto.getQuantity());
        productEntity.setPrice(productsDto.getPrice());
        productEntity.setInvoiceId(savedInvoice.getId());

        productsRepository.save(productEntity);
    }
}
