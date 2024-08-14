package pet.store.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;

    @Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
				petStore.setPetStoreName(petStoreData.getPetStoreName());
				petStore.setPetStoreCity(petStoreData.getPetStoreCity());
				petStore.setPetStoreState(petStoreData.getPetStoreState());
				petStore.setPetStoreZip(petStoreData.getPetStoreZip());
				petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
				
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		}
		else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
				return petStoreDao.findById(petStoreId)
						.orElseThrow(()  -> new NoSuchElementException("Pet store ID = " +
				petStoreId + "could not be found."));
	}
	
// week 15 >>
	
	@Autowired
	private EmployeeDao employeeDao;

	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new IllegalArgumentException("invalid pet store Id"));
	
		
		Employee employee = findOrCreateEmployee(petStoreId, petStoreEmployee.getEmployeeId());
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		
		return new PetStoreEmployee();
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		
		
		
}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		
		if(employeeId == null) {
			return new Employee();
		}else {
			return findEmployeeById(petStoreId, employeeId);
		
		}
		
		
		
}
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("employee not found"));
		
	
		if(!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
			throw new IllegalArgumentException("employee does not belong to pet store");
		}
		return employee;
			
		
	}
	
@Autowired CustomerDao customerDao;

 @Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
	          PetStore petStore = petStoreDao.findById(petStoreId)
					.orElseThrow(() -> new IllegalArgumentException("invalid pet store Id"));
		
				
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());
		
		copyCustomerFields(customer, petStoreCustomer);
		
		if(customer.getPetStores() == null) {
			customer.setPetStores(new HashSet<>());
		}
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		
		return new PetStoreCustomer();
	
 

}
 
 public void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
	 customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
	 customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
	 customer.setCustomerId(petStoreCustomer.getCustomerId());
	 customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());

 
 
}
 
 private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
	 
	 if(customerId == null) {
		 return new Customer();
	 } else {
		 
		 return findCustomerById(petStoreId, customerId);
	 }
 }

private Customer findCustomerById(Long petStoreId, Long customerId) {
	Customer customer = customerDao.findById(customerId)
			.orElseThrow(() -> new NoSuchElementException("customer not found"));
	
	boolean found = customer.getPetStores().stream()
			.anyMatch(store -> store.getPetStoreId().equals(petStoreId));
	
	if (!found) {
		throw new IllegalArgumentException("Customer does not belong to pet Store");
	}
	return customer;
}


@Transactional(readOnly = false)
public List<PetStoreData> retrieveAllPetStores() {
	List<PetStore> petStores = petStoreDao.findAll();
	
	List<PetStoreData> result = new LinkedList<>();
	
	for(PetStore petStore : petStores) {
		PetStoreData psd = new PetStoreData(petStore);
				
	
		result.add(psd);
	}
	 return result;
}


@Transactional(readOnly = false)
public PetStoreData retrievePetStoreById(Long id) {
	PetStore petStore = findPetStoreById(id);
	PetStoreData petStoreData = new PetStoreData(petStore);
	
	
	petStore.getCustomers().forEach(customer -> petStoreData.getCustomers().add(new PetStoreData.PetStoreCustomer()) );
    petStore.getEmployees().forEach(employee -> petStoreData.getEmployees().add(new PetStoreData.PetStoreEmployee()));
	
    return petStoreData;
	
}
@Transactional
public void deletePetStoreById(Long petStoreId) {
	PetStore petStore = petStoreDao.findById(petStoreId)
			.orElseThrow(() -> new NoSuchElementException("pet store with id" + petStoreId + "not found"));
	petStoreDao.delete(petStore);
	
}





}
