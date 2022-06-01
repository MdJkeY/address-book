package kz.ks.storefront.addressbook.repository;

import kz.ks.storefront.addressbook.model.CustomerModel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerModel, Long> {
    Optional<CustomerModel> findByGci(String gci);
}
