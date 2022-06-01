package kz.ks.storefront.addressbook.controller;

import kz.ks.storefront.addressbook.controller.dto.CustomerDTO;
import kz.ks.storefront.addressbook.controller.dto.PersistentAddressDTO;
import kz.ks.storefront.addressbook.controller.dto.PersistentCustomerDTO;
import kz.ks.storefront.addressbook.converter.AddressModelPersistentConverter;
import kz.ks.storefront.addressbook.model.AddressModel;
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
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;

    @GetMapping("/customer/list")
    public List<PersistentCustomerDTO> getList() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(a -> conversionService.convert(a, PersistentCustomerDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerID}")
    public PersistentCustomerDTO findById(@PathVariable("customerID") long customerID) {
        var existingCustomer = customerRepository.findById(customerID);

        return existingCustomer.map(
                a -> conversionService.convert(a, PersistentCustomerDTO.class)
        ).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("/customer")
    public PersistentCustomerDTO create(@RequestBody CustomerDTO customerDTO) {
        var newCustomer = CustomerModel.builder()
                .gci(customerDTO.getGci())
                .build();
        customerRepository.save(newCustomer);

        return conversionService.convert(newCustomer, PersistentCustomerDTO.class);
    }

    @DeleteMapping("/customer/{customerID}")
    public void delete(@PathVariable("customerID") long customerID) {
        customerRepository.findById(customerID)
                .ifPresentOrElse(
                        customerRepository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        });
    }

    @PostMapping("/customer/{customerID}")
    public void update(@PathVariable(name = "customerID") long customerID,
                       @RequestBody CustomerDTO customerDTO) {
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
