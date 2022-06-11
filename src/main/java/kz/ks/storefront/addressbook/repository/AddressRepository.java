package kz.ks.storefront.addressbook.repository;

import kz.ks.storefront.addressbook.model.AddressModel;
import kz.ks.storefront.addressbook.model.CustomerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressModel, Long> {
    Iterable<AddressModel> findByOwnerAndCityIdAndVisible(CustomerModel owner, String cityId, boolean visible);
}
