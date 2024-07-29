package pet.store.controller.model;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {

	private Long petStoreId;
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private String petStoreZip;
	private Set<PetStoreCustomer> customers; 
	private Set<PetStoreEmployee> employees;
	
	public PetStoreData(PetStore petStore) {
		petStoreId = petStore.getPetStoreId();
		petStoreName = petStore.getPetStoreName();
		petStoreAddress = petStore.getPetStoreAddress();
		petStoreCity = petStore.getPetStoreCity();
		petStoreState = petStore.getPetStoreState();
		petStoreZip = petStore.getPetStoreZip();

		
	}

	
	@Data
	@NoArgsConstructor
	public static class PetStoreCustomer {
			
			private Long customerId;
			private String customerFirstName;
			private String customerLastName;
			private String customerEmail;
			private Set<PetStore> petStores;
				
			
}
	@Data
	@NoArgsConstructor
	public static class PetStoreEmployee {
	
		private Long employeeId;
		private String employeeFirstName;
		private String employeeLastName;
		private Long employeePhone;
		private String employeeJobTitle;
		private PetStore petStore;
		
	
	
	}
}
