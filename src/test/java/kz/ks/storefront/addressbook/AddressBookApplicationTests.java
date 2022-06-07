package kz.ks.storefront.addressbook;


import kz.ks.storefront.addressbook.controller.AddressController;
import kz.ks.storefront.addressbook.controller.dto.AddressDTO;
import kz.ks.storefront.addressbook.controller.dto.GeoPointDTO;
import kz.ks.storefront.addressbook.converter.GeoPointDtoConverter;
import kz.ks.storefront.addressbook.enums.CoordinateSystem;
import kz.ks.storefront.addressbook.model.AddressModel;
import kz.ks.storefront.addressbook.model.CustomerModel;
import kz.ks.storefront.addressbook.model.GeoPoint;
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
	private AddressController addressController;
	@Autowired
	private GeoPointDtoConverter geoPointDtoConverter;

	@Test
	@Transactional
	void addressVisibilityTest() {
		String gci = "7359432823";
		String cityId = "78";


		var address = AddressDTO.builder()
				.cityId(cityId)
				.streetName("Abay")
				.house("12")
				.apartment("98")
				.geoPointDTO(
						GeoPointDTO.builder()
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
						List.of(invisibleAddress)
				)
				.build();

		customer.getAddresses().forEach(
				a -> a.setOwner(customer)
		);

		customerRepository.save(customer);

		address.setCustomerGci(customer.getGci());

		var firstResult = addressController.create(address);

		var secondResult = addressController.getListByIdAndCityId(gci, cityId);

		secondResult.add(firstResult);

		Assert.notEmpty(secondResult, "Visible address not returned");
		Assert.isTrue(secondResult.size() == 1, "Not only visible address returned");
		Assert.isTrue(secondResult.get(0).getId().equals(firstResult.getId()), "Wrong address returned");
	}


	@Test
	@Transactional
	void addressCityFilter() {
		String gci = "7359432823";
		String requiredCityId = "102";
		String nonRequiredCityId = "105";

		var firstAddress = AddressModel.builder()
				.cityId(requiredCityId)
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

		var secondAddress = AddressModel.builder()
				.cityId(requiredCityId)
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
				.visible(true)
				.build();

		var invisibleAddress = AddressModel.builder()
				.cityId(nonRequiredCityId)
				.streetName("Abay")
				.house("14")
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

		var customer = CustomerModel.builder()
				.gci(gci)
				.addresses(
						List.of(firstAddress, secondAddress, invisibleAddress)
				)
				.build();

		customer.getAddresses().forEach(
				a -> a.setOwner(customer)
		);

		customerRepository.save(customer);

		var result = addressController.getListByIdAndCityId(gci, requiredCityId);

		Assert.isTrue(result.size() == 2, "Wrong addresses count returned");
		Assert.isTrue(result.get(0).getCityId().equals(firstAddress.getCityId()),
				"Wrong firstAddress cityId returned");
		Assert.isTrue(result.get(1).getCityId().equals(secondAddress.getCityId()),
				"Wrong secondAddress cityId returned");
		Assert.doesNotContain(result.get(result.size() - 1).getCityId(), nonRequiredCityId,
				"Contains non required cityId");
	}


	@Test
	@Transactional
	void geoPointDtoConverterTest() {
		var geoPointDto = GeoPointDTO.builder()
				.lat(100)
				.lon(100)
				.coordinateSystem(CoordinateSystem.WGS84)
				.build();

		var geoPoint = geoPointDtoConverter.convert(geoPointDto);


		Assert.isTrue(geoPointDto.getLat() == geoPoint.getLat(), "Wrong lat parsed");
		Assert.isTrue(geoPointDto.getLon() == geoPoint.getLon(), "Wrong lon parsed");
		Assert.isTrue(geoPointDto.getCoordinateSystem()
				.equals(geoPoint.getCoordinateSystem()), "Wrong coordinate system parsed");
	}


	@Test
	@Transactional
	void addressUpdateTest() {
		var customAddress = AddressModel.builder()
				.cityId("12")
				.streetName("Abay")
				.house("78B")
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

		var customer = CustomerModel.builder()
				.gci("46238422")
				.addresses(
						List.of(customAddress)
				)
				.build();

		customer.getAddresses().forEach(
				a -> a.setOwner(customer)
		);

		customerRepository.save(customer);


		var geoPointDto = GeoPointDTO.builder()
				.lat(90)
				.lon(22)
				.coordinateSystem(CoordinateSystem.WGS84)
				.build();


		var updateAddress = AddressDTO.builder()
				.cityId("89")
				.streetName("Chopin")
				.house("6A")
				.apartment("44")
				.geoPointDTO(geoPointDto)
				.visible(true)
				.customerGci(customer.getGci())
				.build();


		addressController.update(customAddress.getId(), updateAddress);

		var result = customer.getAddresses().get(0);

		Assert.isTrue(result.getCityId().equals(updateAddress.getCityId()),
				"Wrong cityId, value has not been updated");
		Assert.isTrue(result.getStreetName().equals(updateAddress.getStreetName()),
				"Wrong streetName, value has not been updated");
		Assert.isTrue(result.getHouse().equals(updateAddress.getHouse()),
				"Wrong house, value has not been updated");
		Assert.isTrue(result.getApartment().equals(updateAddress.getApartment()),
				"Wrong apartment, value has not been updated");
		Assert.isTrue(result.getGeoPoint().equals(geoPointDtoConverter.convert(updateAddress.getGeoPointDTO())),
				"Wrong geoPoint, value has not been updated");
		Assert.isTrue(result.isVisible() == updateAddress.isVisible(),
				"Wrong visible flag, flag has not been updated");
	}

}
