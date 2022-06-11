package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.AddressDTO;
import kz.ks.storefront.addressbook.controller.dto.GeoPointDTO;
import kz.ks.storefront.addressbook.model.AddressModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressModelConverter implements Converter<AddressModel, AddressDTO> {
    @Autowired
    ConversionService conversionService;

    @Override
    public AddressDTO convert(AddressModel source) {
        return AddressDTO.builder()
                .cityId(source.getCityId())
                .streetName(source.getStreetName())
                .apartment(source.getApartment())
                .house(source.getHouse())
                .visible(source.isVisible())
                .geoPointDTO(conversionService.convert(source.getGeoPoint(), GeoPointDTO.class))
                .build();
    }
}
