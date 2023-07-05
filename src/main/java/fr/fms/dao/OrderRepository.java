package fr.fms.dao;

import fr.fms.entities.Category;
import fr.fms.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
