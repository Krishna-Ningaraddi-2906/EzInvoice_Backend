package com.EzInvoice.backend.service.InvoiceService;

import com.EzInvoice.backend.dto.invoiceDto.CreateInvoiceDto;
import com.EzInvoice.backend.dto.invoiceDto.InvoiceResponseDto;
import com.EzInvoice.backend.dto.invoiceDto.AddProductsDto;
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

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UserRepository userRepository;


    // Create Invoice
    public void createInvoice(CreateInvoiceDto createInvoiceDto, AddProductsDto addProductsDto) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setCustomerName(createInvoiceDto.getCustomerName());
        invoiceEntity.setCustomerEmail(createInvoiceDto.getCustomerEmail());
        invoiceEntity.setCompanyOrIndividual(createInvoiceDto.getCompanyOrIndividual());

        // Get logged-in user, this is used to get the current user logged in and helps in mapping the invoices to user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            invoiceEntity.setUser(user); // <-- link invoice to user
        }

        InvoiceEntity savedInvoice = invoiceRepository.save(invoiceEntity);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(addProductsDto.getProductName());
        productEntity.setQuantity(addProductsDto.getQuantity());
        productEntity.setPrice(addProductsDto.getPrice());
        productEntity.setInvoiceId(savedInvoice.getId());

        productsRepository.save(productEntity);
    }


    // Get All invoices
    public List<InvoiceResponseDto> getAllInvoicesForUser(String userEmail) {
        // Find the user by email
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all invoices linked to this user
        List<InvoiceEntity> invoices = invoiceRepository.findByUser(user);

        // Map to DTO to avoid sending full user info
        return invoices.stream().map(invoice -> {
            InvoiceResponseDto dto = new InvoiceResponseDto();
            dto.setId(invoice.getId());
            dto.setCustomerName(invoice.getCustomerName());
            dto.setCompanyOrIndividual(invoice.getCompanyOrIndividual());
            dto.setCustomerEmail(invoice.getCustomerEmail());
            dto.setCreatedAt(invoice.getCreatedAt());
            dto.setUpdatedAt(invoice.getUpdatedAt());
            return dto;
        }).toList();
    }


    // Get invoice by id
    public List<InvoiceResponseDto> getInvoicesForUserByCustomerEmail(String userEmail, String customerEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<InvoiceEntity> invoices = invoiceRepository.findByUserAndCustomerEmail(user, customerEmail);

        return invoices.stream().map(invoice -> {
            InvoiceResponseDto dto = new InvoiceResponseDto();
            dto.setId(invoice.getId());
            dto.setCustomerName(invoice.getCustomerName());
            dto.setCompanyOrIndividual(invoice.getCompanyOrIndividual());
            dto.setCustomerEmail(invoice.getCustomerEmail());
            dto.setCreatedAt(invoice.getCreatedAt());
            dto.setUpdatedAt(invoice.getUpdatedAt());
            return dto;
        }).toList();
    }


    // update invoice
    public boolean updateInvoice(String invoiceId, String userEmail,
                                 CreateInvoiceDto createInvoiceDto, List<AddProductsDto> addProductsDtoList) {

        // 1. Fetch the invoice
        InvoiceEntity invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) return false;

        // 2. Verify ownership
        if (!invoice.getUser().getEmail().equals(userEmail)) return false;

        // 3. Update invoice fields only if new data is provided
        if (createInvoiceDto.getCustomerName() != null &&
                !createInvoiceDto.getCustomerName().equals(invoice.getCustomerName())) {
            invoice.setCustomerName(createInvoiceDto.getCustomerName());
        }

        if (createInvoiceDto.getCustomerEmail() != null &&
                !createInvoiceDto.getCustomerEmail().equals(invoice.getCustomerEmail())) {
            invoice.setCustomerEmail(createInvoiceDto.getCustomerEmail());
        }

        if (createInvoiceDto.getCompanyOrIndividual() != null &&
                !createInvoiceDto.getCompanyOrIndividual().equals(invoice.getCompanyOrIndividual())) {
            invoice.setCompanyOrIndividual(createInvoiceDto.getCompanyOrIndividual());
        }

        // 4. Update products only if new list is provided
        if (addProductsDtoList != null && !addProductsDtoList.isEmpty()) {
            // Delete existing products
            List<ProductEntity> existingProducts = productsRepository.findByInvoiceId(invoiceId);
            productsRepository.deleteAll(existingProducts);

            // Add new products
            for (AddProductsDto productDto : addProductsDtoList) {
                ProductEntity product = new ProductEntity();
                product.setInvoiceId(invoice.getId());
                product.setProductName(productDto.getProductName());
                product.setQuantity(productDto.getQuantity());
                product.setPrice(productDto.getPrice());
                productsRepository.save(product);
            }
        }

        // 5. Save updated invoice
        invoiceRepository.save(invoice);

        return true;
    }

    public boolean deleteInvoice(String invoiceId, String userEmail) {
        InvoiceEntity invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) return false;

        if (!invoice.getUser().getEmail().equals(userEmail)) return false;

        // Delete associated products first
        List<ProductEntity> products = productsRepository.findByInvoiceId(invoiceId);
        productsRepository.deleteAll(products);

        // Delete invoice
        invoiceRepository.delete(invoice);

        return true;
    }


}
