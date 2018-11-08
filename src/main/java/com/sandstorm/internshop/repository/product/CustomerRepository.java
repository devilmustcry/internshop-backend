package com.sandstorm.internshop.repository.product;

import com.sandstorm.internshop.entity.product.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Lock(value = LockModeType.READ)
    Optional<Customer> findByUsername(String username);
}
