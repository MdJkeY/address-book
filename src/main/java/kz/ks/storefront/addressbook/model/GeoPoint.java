package kz.ks.storefront.addressbook.model;

import kz.ks.storefront.addressbook.enums.CoordinateSystem;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Embeddable
@Data
@Builder
@NoArgsConstructor
public class GeoPoint {
    private double lat;
    private double lon;
    private CoordinateSystem coordinateSystem;


    public GeoPoint(double lat, double lon, CoordinateSystem coordinateSystem) {
        this.lat = lat;
        this.lon = lon;
        this.coordinateSystem = coordinateSystem;
    }
}
