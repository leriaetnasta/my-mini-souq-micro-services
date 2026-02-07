package net.lou.billingservice.web;

import net.lou.billingservice.entities.Bill;
import net.lou.billingservice.feign.CustomerRestClient;
import net.lou.billingservice.feign.ProductRestClient;
import net.lou.billingservice.repository.BillRepository;
import net.lou.billingservice.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
//permet de consulter une facture complete
@RestController
public class BillRestController {
    //injection de dependence (alternative est d'utiliser un constructeur)
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private CustomerRestClient customerRestClient;
    @Autowired
    private ProductRestClient productRestClient;
    //returns a bill
    @GetMapping(path = "/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        //chercher une facture dans la base de données
        Bill bill = billRepository.findById(id).get();
        //retieve the entire customer details
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
        });
        return bill;
    }
}
