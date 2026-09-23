package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.repository.CurrencyRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public List<CurrencyResponse> getAllCurrencies() {
        return currencyRepository.findAll()
            .stream()
            .map(this::toCurrencyResponse)
            .toList();
    }

    private CurrencyResponse toCurrencyResponse(Currency currency) {
        return new CurrencyResponse(
            currency.getId(),
            currency.getCode(),
            currency.getName(),
            currency.getSymbol()
        );
    }
}