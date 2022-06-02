package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.GeoPointDTO;
import kz.ks.storefront.addressbook.model.GeoPoint;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GeoPointDtoConverter implements Converter<GeoPointDTO, GeoPoint> {

    @Override
    public GeoPoint convert(GeoPointDTO source) {
        return GeoPoint.builder()
                .lat(source.getLat())
                .lon(source.getLon())
                .coordinateSystem(source.getCoordinateSystem())
                .build();
    }
}
