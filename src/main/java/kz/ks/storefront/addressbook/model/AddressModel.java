package kz.ks.storefront.addressbook.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Address")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressModel extends BaseEntity {
    @Column(name = "city_id")
    private String cityId;
    @Column(name = "street_name")
    private String streetName;
    private String house;
    private String apartment;

    @Embedded
    private GeoPoint geoPoint;

    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private CustomerModel owner;
}
