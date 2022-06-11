package kz.ks.storefront.addressbook.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "Customer",
        indexes = {
                @Index(columnList = "gci", unique = true)
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerModel extends BaseEntity {
    @Column(unique = true)
    private String gci;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.ALL})
    private List<AddressModel> addresses;


    public List<AddressModel> getAddresses() {
        return addresses;
    }


    public List<AddressModel> getAddressesByCityId(String cityId) {
        List<AddressModel> addressModels = new ArrayList<>();
        for (AddressModel addressModel : addresses) {
            if (addressModel.isVisible() && addressModel.getCityId().equals(cityId)) {
                addressModels.add(addressModel);
            }
        }


        return addressModels;
    }
}
