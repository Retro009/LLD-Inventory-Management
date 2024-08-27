package com.example.ecom.services;

import com.example.ecom.exceptions.ProductNotFoundException;
import com.example.ecom.exceptions.UnAuthorizedAccessException;
import com.example.ecom.exceptions.UserNotFoundException;
import com.example.ecom.models.Inventory;
import com.example.ecom.models.Product;
import com.example.ecom.models.User;
import com.example.ecom.models.UserType;
import com.example.ecom.repositories.InventoryRepository;
import com.example.ecom.repositories.ProductRepository;
import com.example.ecom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class InventoryServiceImpl implements InventoryService{
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Inventory createOrUpdateInventory(int userId, int productId, int quantity) throws ProductNotFoundException, UserNotFoundException, UnAuthorizedAccessException {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not found"));
        if(user.getUserType() != UserType.ADMIN)
            throw new UnAuthorizedAccessException("ACCESS DENIED");
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
        Optional<Inventory> existingInventory = inventoryRepository.findByProductId(productId);
        Inventory inventory;
        if(existingInventory.isEmpty()){
            inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
            return inventoryRepository.save(inventory);
        }
        inventory = existingInventory.get();
        inventory.setQuantity(inventory.getQuantity()+quantity);
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(int userId, int productId) throws UserNotFoundException, UnAuthorizedAccessException {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not found"));
        if(user.getUserType() != UserType.ADMIN)
            throw new UnAuthorizedAccessException("ACCESS DENIED");
        inventoryRepository.delete(inventoryRepository.findByProductId(productId).get());
    }
}
