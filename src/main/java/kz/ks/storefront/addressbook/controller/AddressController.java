package kz.ks.storefront.addressbook.controller;

import kz.ks.storefront.addressbook.controller.dto.AddressDTO;
import kz.ks.storefront.addressbook.controller.dto.PersistentAddressDTO;
import kz.ks.storefront.addressbook.converter.AddressModelPersistentConverter;
import kz.ks.storefront.addressbook.converter.GeoPointDtoConverter;
import kz.ks.storefront.addressbook.model.AddressModel;
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
public class AddressController {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;
    private final GeoPointDtoConverter geoPointDtoConverter;
    private final AddressModelPersistentConverter addressModelPersistentConverter;

    @GetMapping("/address/find")
    public List<PersistentAddressDTO> getListByIdAndCityId(@RequestParam("cid") String customerId,
                                                           @RequestParam("cityId") String cityId) {
        var existingCustomer = customerRepository.findByGci(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        AddressModelPersistentConverter addressModelPersistentConverter = new AddressModelPersistentConverter();
        return addressModelPersistentConverter.converting(existingCustomer.getAddressesByCityId(cityId));
    }

    @GetMapping("/address/{addressId}")
    public PersistentAddressDTO findById(@PathVariable("addressId") long addressId) {
        var existingAddress = addressRepository.findById(addressId);

        return existingAddress.map(
                addressModelPersistentConverter::convert
        ).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("/address")
    public PersistentAddressDTO create(@RequestBody AddressDTO addressDTO) {
//        Clarification is required!!!
        var existingCustomer =
                customerRepository.findByGci(addressDTO.getCustomerGci())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                        );

        var newAddress = Objects.requireNonNull(conversionService.convert(addressDTO, AddressModel.class));

        assert newAddress != null;
        newAddress.setOwner(existingCustomer);

        addressRepository.save(newAddress);


        return addressModelPersistentConverter.convert(newAddress);
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
                    a.setGeoPoint(geoPointDtoConverter.convert(addressDTO.getGeoPointDTO()));
                    addressRepository.save(a);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
        );
    }
}
