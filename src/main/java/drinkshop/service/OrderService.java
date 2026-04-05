package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;

import java.util.List;

public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;

    public OrderService(Repository<Integer, Order> orderRepo, Repository<Integer, Product> productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;

    }

    public void addOrder(Order o) {
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
//        return StreamSupport.stream(orderRepo.findAll().spliterator(), false)
//                .collect(Collectors.toList());
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

    public double computeTotal(Order o) {
        double total = 0.0; // 1

        if (o == null || o.getItems() == null) { // 2
            return 0.0; // 3
        }

        for (OrderItem i : o.getItems()) { // 4
            if (i == null || i.getProduct() == null) { // 5
                continue; // 6
            }

            Product p = productRepo.findOne(i.getProduct().getId()); // 7

            if (p == null) { // 8
                continue; // 9
            }

            double price = p.getPret(); // 10
            int quantity = i.getQuantity(); // 11

            if (price > 0 && quantity > 0) { // 12
                total += price * quantity; // 13
            } else if (price <= 0 || quantity <= 0) { // 14
                continue; // 15
            }
        }

        return total; // 16
    }

    public void addItem(Order o, OrderItem item) {
        o.getItems().add(item);
        orderRepo.update(o);
    }

    public void removeItem(Order o, OrderItem item) {
        o.getItems().remove(item);
        orderRepo.update(o);
    }
}