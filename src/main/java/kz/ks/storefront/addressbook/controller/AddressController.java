package kz.ks.storefront.addressbook.controller;

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

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;

    @GetMapping("/address/find")
    public List<PersistentAddressDTO> getListByIdAndCityId(@RequestParam("cid") String customerId,
                                                           @RequestParam("cityId") String cityId) {
        var existingCustomer = customerRepository.findByGci(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        AddressModelPersistentConverter addressModelPersistentConverter = new AddressModelPersistentConverter();
        return addressModelPersistentConverter.convert(existingCustomer.getAddressesByCityId(cityId));
    }

    @GetMapping("/address/{addressId}")
    public PersistentAddressDTO findById(@PathVariable("addressId") long addressId) {
        var existingAddress = addressRepository.findById(addressId);

        return existingAddress.map(
                a -> conversionService.convert(a, PersistentAddressDTO.class)
        ).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("/address")
    public PersistentAddressDTO create(@RequestBody AddressDTO addressDTO) {
        var existingCustomer =
                customerRepository.findByGci(addressDTO.getCustomerGci())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                        );

        var newAddress = AddressModel.builder()
                .cityId(addressDTO.getCityId())
                .streetName(addressDTO.getStreetName())
                .apartment(addressDTO.getApartment())
                .house(addressDTO.getHouse())
                .owner(existingCustomer)
                .visible(true)
                .geoPoint(GeoPoint.builder()
                        .coordinateSystem(addressDTO.getCs())
                        .lon(addressDTO.getLon())
                        .lat(addressDTO.getLat())
                        .build())
                .build();
        addressRepository.save(newAddress);

        return conversionService.convert(newAddress, PersistentAddressDTO.class);
    }

    @DeleteMapping("/address/{addressId}")
    public void delete(@PathVariable("addressId") long addressId) {
        addressRepository.findById(addressId)
                .ifPresentOrElse(
                        addressRepository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        });
    }

    @PostMapping("/address/{addressId}")
    public void update(@PathVariable("addressId") long addressId,
                       @RequestBody AddressDTO addressDTO) {
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
                    a.setGeoPoint(
                            GeoPoint.builder()
                                    .lat(addressDTO.getLat())
                                    .lon(addressDTO.getLon())
                                    .coordinateSystem(addressDTO.getCs())
                                    .build()
                    );
                    addressRepository.save(a);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
        );
    }
}
