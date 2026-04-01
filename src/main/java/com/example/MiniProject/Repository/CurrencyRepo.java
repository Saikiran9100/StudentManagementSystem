package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CurrencyRepo extends JpaRepository<Currency,Long> {
    Optional<Currency> findByCurrencyCode(String currencyCode);

    boolean existsByCurrencyCode(String currencyCode);
}
