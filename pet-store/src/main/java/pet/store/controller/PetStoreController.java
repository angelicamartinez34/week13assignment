package pet.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;
	
@PostMapping("/petStore")
@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore 
	(@RequestBody PetStoreData petStoreData) {
	log.info("...pet store...", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
@PutMapping("/petStore/{petStoreId}")
public PetStoreData updatePetStore(@PathVariable Long petStoreId, 
		@RequestBody PetStoreData petStoreData) {
	petStoreData.setPetStoreId(petStoreId);
	log.info("updating PetStore {}", petStoreData);
	return petStoreService.savePetStore(petStoreData);
}

// add pet store employee mothod

@PostMapping("/petStore/{petStoreId}/employee")
@ResponseStatus(code = HttpStatus.CREATED)
public PetStoreEmployee addEmployee(@PathVariable Long petStoreId,
		@RequestBody PetStoreEmployee employee) {
	log.info("...adding employee...", petStoreId);
	return petStoreService.saveEmployee(petStoreId, employee);
	
}

// add pet store customer method

@PostMapping("/petStore/{petStoreId}/customer")
@ResponseStatus(code = HttpStatus.CREATED)
public PetStoreCustomer addCustomer(@PathVariable Long petStoreId,
		@RequestBody PetStoreCustomer customer) {
	log.info("...adding customer...", petStoreId);
	return petStoreService.saveCustomer(petStoreId, customer);
	
}

// list all pet stores method

@GetMapping("/")
public List<PetStoreData> listAllPetStores() {
	return petStoreService.retrieveAllPetStores();



}

// retrive pet store by ID method

@GetMapping("/{id}")
public PetStoreData getPetStoreById(@PathVariable Long id) {
	return petStoreService.retrievePetStoreById(id);

}
@PostConstruct
public void init() {
	System.out.println("petstorecontroller inistialized");
}

// delete a pet store by id method

@DeleteMapping("/{petStoreId}")
public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
	log.info("...deleting pet store...", petStoreId);
	petStoreService.deletePetStoreById(petStoreId);
	Map<String, String> response = new HashMap<>();
	response.put("message", "pet store with Id " + petStoreId + "was succesfully deleted.");
	return response;
}

}
