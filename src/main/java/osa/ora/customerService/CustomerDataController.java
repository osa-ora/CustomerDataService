package osa.ora.customerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.ora.beans.Accounts;
import osa.ora.beans.Customer;
import osa.ora.customer.exception.JsonMessage;
import osa.ora.customer.persistence.CustomerAccountsPersistence;
import osa.ora.customer.persistence.CustomerPersistence;

@RestController
@RequestMapping("/api/v1")
public class CustomerDataController {
    private final CustomerPersistence customerPersistence = new CustomerPersistence();
    private final CustomerAccountsPersistence customerAccountPersistence = new CustomerAccountsPersistence();

    public CustomerDataController() {
		super();
	}

	@GetMapping("/customers/all")
	public Customer[] getUserAllCustomers() {
		System.out.println("Load all customers..");
        Customer[] customers=customerPersistence.findAll();
        if(customers!=null && customers.length>0){
            for(Customer customer:customers){
                Accounts[] accounts=customerAccountPersistence.findbyId(customer.getId());
                customer.setCustomerAccounts(accounts);
            }
            return customers;
        }else{
            return null;            
        }
	}
	
	@GetMapping("/customers/{email}")
	public Customer getCustomerByEmail(@PathVariable(value = "email") String email) {
		Customer customer = customerPersistence.findbyEmail(email);
        if (customer != null) {
            System.out.println("Retireve customer using: " + email);
            Accounts[] accounts=customerAccountPersistence.findbyId(customer.getId());
            customer.setCustomerAccounts(accounts);
            return customer;
        } else {
            return null;
        }
	}
    @PostMapping("/customers/add")
	public boolean addCustomer(@RequestBody Customer customer) {
    	JsonMessage jsonMessage = customerPersistence.save(customer);
        if (jsonMessage.getType().equals("Success")) {
            System.out.println("Successfully added a new customer");
            return true;
        } else {
            return false;
        }
	}
    @PostMapping("/customers/{id}/update")
	public boolean updateCustomer(@PathVariable(value = "id") long id,@RequestBody Customer customer) {
    	customer.setId(id);
        Customer cust = customerPersistence.findbyId(customer.getId());
        if (cust != null) {
            JsonMessage jsm = customerPersistence.update(customer);
            if (jsm.getType().equals("Success")) {
                System.out.println("Successfully updated customer with id=" + id);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
	}
    @PostMapping("/customers/{id}/remove")
	public boolean removeCustomer(@PathVariable(value = "id") long id,@RequestBody Customer customer) {
    	Customer cust = customerPersistence.findbyId(id);
        if (cust != null) {
            JsonMessage jsm = customerPersistence.delete(id);
            if (jsm.getType().equals("Success")) {
                System.out.println("Successfully deleted user with id=" + id);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
	}
}
