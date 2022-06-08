package kz.ks.storefront.addressbook.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ks.storefront.addressbook.controller.dto.AddressDTO;
import kz.ks.storefront.addressbook.controller.dto.PersistentAddressDTO;
import kz.ks.storefront.addressbook.converter.AddressModelPersistentConverter;
import kz.ks.storefront.addressbook.model.AddressModel;
import kz.ks.storefront.addressbook.model.GeoPoint;
import kz.ks.storefront.addressbook.repository.AddressRepository;
import kz.ks.storefront.addressbook.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "Addresses",
        description = "CRUD operations on addresses")
public class AddressController {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;
    private final AddressModelPersistentConverter addressModelPersistentConverter;

    @GetMapping("/address/find")
    @Operation(summary = "Get addresses by customer. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersistentAddressDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "No such customer",
                    content = @Content)})
    public List<PersistentAddressDTO> getListByIdAndCityId(
            @RequestParam("cid")
            @Parameter(
                    description = "Customer ID",
                    example = "103395280",
                    required = true
            )
            String customerId,
            @RequestParam("cityID")
            @Parameter(
                    description = "City ID",
                    example = "23",
                    required = true
            )
            String cityId) {
        var existingCustomer = customerRepository.findByGci(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        AddressModelPersistentConverter addressModelPersistentConverter = new AddressModelPersistentConverter();
        return addressModelPersistentConverter.converting(existingCustomer.getAddressesByCityId(cityId));
    }

    @GetMapping("/address/{addressId}")
    @Operation(summary = "Get address information by address-ID. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersistentAddressDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No such address",
                    content = @Content)})
    public PersistentAddressDTO findById(
            @PathVariable("addressID")
            @Parameter(
                    description = "address ID",
                    example = "103",
                    required = true
            )
            long addressId) {
        var existingAddress = addressRepository.findById(addressId);

        return existingAddress.map(
                addressModelPersistentConverter::convert
        ).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("/address")
    @Operation(summary = "Creating address by Address Data Transfer Object. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersistentAddressDTO.class))}),
            @ApiResponse(responseCode = "400", description = "No such customer for owner field",
                    content = @Content)})
    public PersistentAddressDTO create(
            @RequestBody
            @Parameter(
                    description = "Address Data Transfer Object",
                    required = true
            )
            AddressDTO addressDTO) {
        var existingCustomer =
                customerRepository.findByGci(addressDTO.getCustomerGci())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                        );

        var newAddress = Objects.requireNonNull(conversionService.convert(addressDTO, AddressModel.class));
        newAddress.setOwner(existingCustomer);

        addressRepository.save(newAddress);


        return conversionService.convert(newAddress, PersistentAddressDTO.class);
    }

    @DeleteMapping("/address/{addressId}")
    @Operation(summary = "Deleting address by Address ID. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = void.class))}),
            @ApiResponse(responseCode = "404", description = "No such address",
                    content = @Content)})
    public void delete(
            @PathVariable("addressId")
            @Parameter(
                    description = "Address ID",
                    example = "678",
                    required = true
            )
            long addressId) {
        addressRepository.findById(addressId)
                .ifPresentOrElse(
                        addressRepository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        });
    }

    @PostMapping("/address/{addressId}")
    @Operation(summary = "Updating address by Address-ID and Address Data Transfer Object. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = void.class))}),
            @ApiResponse(responseCode = "400", description = "No such customer for owner field",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No such address",
                    content = @Content)})
    public void update(
            @PathVariable("addressId")
            @Parameter(
                    description = "Address ID",
                    example = "9001",
                    required = true
            )
            long addressId,
            @RequestBody
            @Parameter(
                    description = "Address Data Transfer Object",
                    required = true
            )
            AddressDTO addressDTO) {
        var existingCustomer =
                customerRepository.findByGci(addressDTO.getCustomerGci())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                        );

        var existingAddress = addressRepository.findById(addressId);

        existingAddress.ifPresentOrElse(
                a -> {
                    a.setCityId(addressDTO.getCityId());
                    a.setStreetName(addressDTO.getStreetName());
                    a.setHouse(addressDTO.getHouse());
                    a.setApartment(addressDTO.getApartment());
                    a.setOwner(existingCustomer);
                    a.setVisible(addressDTO.isVisible());
                    a.setGeoPoint(conversionService.convert(addressDTO.getGeoPointDTO(), GeoPoint.class));
                    addressRepository.save(a);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
        );
    }
}
