package kz.ks.storefront.addressbook;


import kz.ks.storefront.addressbook.controller.AddressController;
import kz.ks.storefront.addressbook.enums.CoordinateSystem;
import kz.ks.storefront.addressbook.model.AddressModel;
import kz.ks.storefront.addressbook.model.CustomerModel;
import kz.ks.storefront.addressbook.model.GeoPoint;
import kz.ks.storefront.addressbook.repository.AddressRepository;
import kz.ks.storefront.addressbook.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;


@SpringBootTest
class AddressBookApplicationTests {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressController addressController;

	@Test
	@Transactional
	void addressVisibilityTest() {
		String gci = "7359432823";
		String cityId = "78";

		var address = AddressModel.builder()
				.cityId(cityId)
				.streetName("Abay")
				.house("12")
				.apartment("98")
				.geoPoint(
						GeoPoint.builder()
								.lat(51)
								.lon(71)
								.coordinateSystem(CoordinateSystem.WGS84)
								.build()
				)
				.visible(true)
				.build();

		var invisibleAddress = AddressModel.builder()
				.cityId(cityId)
				.streetName("Abay")
				.house("13")
				.apartment("98")
				.geoPoint(
						GeoPoint.builder()
								.lat(51)
								.lon(71)
								.coordinateSystem(CoordinateSystem.WGS84)
								.build()
				)
				.visible(false)
				.build();

		var customer = CustomerModel.builder()
				.gci(gci)
				.addresses(
						List.of(address, invisibleAddress)
				)
				.build();

		customer.getAddresses().forEach(
				a -> a.setOwner(customer)
		);

		customerRepository.save(customer);

		var result = addressController.getListByIdAndCityId(gci, cityId);

		Assert.notEmpty(result, "Visible address not returned");
		Assert.isTrue(result.size() == 1, "Not only visible address returned");
		Assert.isTrue(result.get(0).getId().equals(address.getId()), "Wrong address returned");
	}


	@Test
	@Transactional
	void addressCityFilter() {

	}

}
