package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.GeoPointDTO;
import kz.ks.storefront.addressbook.model.GeoPoint;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GeoPointConverter implements Converter<GeoPoint, GeoPointDTO> {

    @Override
    public GeoPointDTO convert(GeoPoint source) {
        return GeoPointDTO.builder()
                .lat(source.getLat())
                .lon(source.getLon())
                .coordinateSystem(source.getCoordinateSystem())
                .build();
    }
}
