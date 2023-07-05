package fr.fms.dao;

import fr.fms.entities.Category;
import fr.fms.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
