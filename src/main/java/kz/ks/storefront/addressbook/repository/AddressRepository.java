package kz.ks.storefront.addressbook.repository;

import kz.ks.storefront.addressbook.model.AddressModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressModel, Long> {
}
