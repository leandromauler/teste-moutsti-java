package br.com.leandroauler.process.orders.repository;

import br.com.leandroauler.process.orders.entity.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder, UUID> {
}
