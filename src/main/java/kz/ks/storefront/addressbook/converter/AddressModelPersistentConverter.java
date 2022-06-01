package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.PersistentAddressDTO;
import kz.ks.storefront.addressbook.model.AddressModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressModelPersistentConverter implements Converter<AddressModel, PersistentAddressDTO> {
    @Override
    public PersistentAddressDTO convert(AddressModel source) {
        return PersistentAddressDTO.builder()
                .id(source.getId())
                .cityId(source.getCityId())
                .streetName(source.getStreetName())
                .apartment(source.getApartment())
                .house(source.getHouse())
                .visible(source.isVisible())
                .lat(source.getGeoPoint().getLat())
                .lon(source.getGeoPoint().getLon())
                .coordinateSystem(source.getGeoPoint().getCoordinateSystem())
                .build();
    }


    public List<PersistentAddressDTO> convert(List<AddressModel> source) {
        List<PersistentAddressDTO> persistentAddressDTOS = new ArrayList<>();

        for(AddressModel s : source) {
            persistentAddressDTOS.add(convert(s));
        }


        return persistentAddressDTOS;
    }
}
