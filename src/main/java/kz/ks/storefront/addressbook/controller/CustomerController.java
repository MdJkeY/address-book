package kz.ks.storefront.addressbook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ks.storefront.addressbook.controller.dto.CustomerDTO;
import kz.ks.storefront.addressbook.controller.dto.PersistentCustomerDTO;
import kz.ks.storefront.addressbook.model.CustomerModel;
import kz.ks.storefront.addressbook.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@Tag(name = "Customers",
        description = "CRUD operations on customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;

    @GetMapping("/customer/list")
    @Operation(summary = "Get customer list. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersistentCustomerDTO.class)))})})
    public List<PersistentCustomerDTO> getList() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(a -> conversionService.convert(a, PersistentCustomerDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerID}")
    @Operation(summary = "Get customer information by Customer-ID. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersistentCustomerDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No such customer",
                    content = @Content)})
    public PersistentCustomerDTO findById(
            @PathVariable("customerID")
            @Parameter(
                    description = "Customer ID",
                    example = "95280",
                    required = true
            )
            long customerID) {
        var existingCustomer = customerRepository.findById(customerID);

        return existingCustomer.map(
                a -> conversionService.convert(a, PersistentCustomerDTO.class)
        ).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("/customer")
    @Operation(summary = "Creating customer by Customer Data Transfer Object. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersistentCustomerDTO.class))})})
    public PersistentCustomerDTO create(
            @RequestBody
            @Parameter(
                    description = "Customer Data Transfer Object",
                    required = true
            )
            CustomerDTO customerDTO) {
        var newCustomer = CustomerModel.builder()
                .gci(customerDTO.getGci())
                .build();
        customerRepository.save(newCustomer);

        return conversionService.convert(newCustomer, PersistentCustomerDTO.class);
    }

    @DeleteMapping("/customer/{customerID}")
    @Operation(summary = "Deleting customer by Customer-ID. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersistentCustomerDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No such customer",
                    content = @Content)})
    public void delete(
            @PathVariable("customerID")
            @Parameter(
                    description = "Customer ID",
                    example = "800",
                    required = true
            )
            long customerID) {
        customerRepository.findById(customerID)
                .ifPresentOrElse(
                        customerRepository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        });
    }

    @PostMapping("/customer/{customerID}")
    @Operation(summary = "Updating customer by Customer-ID. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersistentCustomerDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No such customer",
                    content = @Content)})
    public void update(
            @PathVariable(name = "customerID")
            @Parameter(
                    description = "Customer ID",
                    example = "1033",
                    required = true
            )
            long customerID,
            @RequestBody
            @Parameter(
                    description = "Customer Data Transfer Object",
                    required = true
            )
            CustomerDTO customerDTO) {
        var existingCustomer = customerRepository.findById(customerID);

        existingCustomer.ifPresentOrElse(
                a -> {
                    a.setGci(customerDTO.getGci());
                    customerRepository.save(a);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
        );
    }
}
