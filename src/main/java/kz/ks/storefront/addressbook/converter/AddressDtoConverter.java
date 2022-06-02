package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.AddressDTO;
import kz.ks.storefront.addressbook.model.AddressModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoConverter implements Converter<AddressDTO, AddressModel> {
    @Autowired
    GeoPointDtoConverter geoPointDtoConverter;

    @Override
    public AddressModel convert(AddressDTO source) {
        return AddressModel.builder()
                .cityId(source.getCityId())
                .streetName(source.getStreetName())
                .apartment(source.getApartment())
                .house(source.getHouse())
                .visible(source.isVisible())
                .geoPoint(geoPointDtoConverter.convert(source.getGeoPointDTO()))
                .build();
    }
}
