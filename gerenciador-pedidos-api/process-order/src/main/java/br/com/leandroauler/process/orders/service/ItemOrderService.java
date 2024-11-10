package br.com.leandroauler.process.orders.service;

import br.com.leandroauler.process.orders.entity.ItemOrder;
import br.com.leandroauler.process.orders.entity.Orders;
import org.springframework.stereotype.Service;
import br.com.leandroauler.process.orders.repository.ItemOrderRepository;

import java.util.List;

@Service
public class ItemOrderService {

    private final ItemOrderRepository itemOrderRepository;

    public ItemOrderService(ItemOrderRepository itemOrderRepository) {
        this.itemOrderRepository = itemOrderRepository;
    }

    public List<ItemOrder> save(List<ItemOrder> itens) {
        return itemOrderRepository.saveAll(itens);
    }

    public void save(ItemOrder item) {
        itemOrderRepository.save(item);
    }

    public void updatedItemPedido(List<ItemOrder> itemOrders, Orders order) {

        itemOrders.forEach(item -> {
            item.setOrders(order);
            this.save(item);
        });

    }
}
