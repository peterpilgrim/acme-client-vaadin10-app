package com.xenonsoft.client.acme.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByLastNameStartsWithIgnoreCase(String lastName);
}
