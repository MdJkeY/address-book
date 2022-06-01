package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.AddressDTO;
import kz.ks.storefront.addressbook.model.AddressModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressModelConverter implements Converter<AddressModel, AddressDTO> {
    @Override
    public AddressDTO convert(AddressModel source) {
        return AddressDTO.builder()
                .cityId(source.getCityId())
                .streetName(source.getStreetName())
                .apartment(source.getApartment())
                .house(source.getHouse())
                .visible(source.isVisible())
                .lat(source.getGeoPoint().getLat())
                .lon(source.getGeoPoint().getLon())
                .cs(source.getGeoPoint().getCoordinateSystem())
                .build();
    }
}
