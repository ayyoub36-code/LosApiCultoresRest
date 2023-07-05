package fr.fms.dao;

import fr.fms.entities.Category;
import fr.fms.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
