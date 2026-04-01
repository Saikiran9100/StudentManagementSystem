package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.Requests.CurrencyRequestDto;
import com.example.MiniProject.Dto.Responses.CurrencyResponseDto;
import com.example.MiniProject.Entity.Currency;
import com.example.MiniProject.Mapper.CurrencyMapper;
import com.example.MiniProject.Repository.CurrencyRepo;
import com.example.MiniProject.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyMapper currencyMapper;
    private final CurrencyRepo currencyRepo;



    public ApiResponse<CurrencyResponseDto> addCurrency(
            CurrencyRequestDto currencyRequestDto
    ) {

        try {

            log.info("Attempting to add values in Currency table");
            String currencyCode = currencyRequestDto
                    .getCurrencyCode()
                    .trim()
                    .toUpperCase();

            log.info("Normalized currency code: {}", currencyCode);
            if (currencyRepo.existsByCurrencyCode(currencyCode)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Currency code already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            Currency currency = currencyMapper.toEntity(currencyRequestDto);

            currency.setCurrencyCode(currencyCode);
            if (currencyCode.equals("INR")) {
                currency.setValueInr(1.0);
            }

            Currency saved = currencyRepo.save(currency);

            log.info("Currency created successfully with id {}", saved.getId());

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Currency created successfully",
                    currencyMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Error while creating currency: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while creating currency",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<CurrencyResponseDto> getCurrencyById(Long currencyId) {

        log.info("Fetching currency with id {}", currencyId);

        try {
            if (currencyId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Currency id is required",
                        null,
                        LocalDateTime.now()
                );
            }
            Currency currency = currencyRepo.findById(currencyId)
                    .orElseThrow(() ->
                            new RuntimeException("Currency not found with id " + currencyId)
                    );
            CurrencyResponseDto responseDto = currencyMapper.toDto(currency);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Currency fetched successfully",
                    responseDto,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Fetching currency failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while fetching currency", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while fetching currency",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<CurrencyResponseDto> updateCurrency(
            Long id,
            CurrencyRequestDto currencyRequestDto
    ) {

        log.info("Attempting to update currency with id {}", id);

        try {
            String newCode = currencyRequestDto.getCurrencyCode()
                    .trim()
                    .toUpperCase();

            Currency currency = currencyRepo.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Currency not found with id " + id)
                    );
            if (currency.getCurrencyCode().equals("INR")) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Base currency INR cannot be modified",
                        null,
                        LocalDateTime.now()
                );
            }


            if (!currency.getCurrencyCode().equals(newCode)) {

                if (currencyRepo.existsByCurrencyCode(newCode)) {
                    return new ApiResponse<>(
                            false,
                            HttpStatus.CONFLICT,
                            "Currency code already exists",
                            null,
                            LocalDateTime.now()
                    );
                }

                currency.setCurrencyCode(newCode);
            }
            currency.setValueInr(currencyRequestDto.getValueInr());

            Currency updated = currencyRepo.save(currency);

            log.info("Currency updated successfully with id {}", updated.getId());

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Currency updated successfully",
                    currencyMapper.toDto(updated),
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Error while updating currency: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while updating currency",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<CurrencyResponseDto>> getAllCurrencies() {
        try{
            log.info("trying to fetch all currencies");
            List<Currency> currencies=currencyRepo.findAll();
            List<CurrencyResponseDto> response=currencyMapper.toDtoList(currencies);
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Currencies Fetched Successfully",
                    response,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            log.warn("Failed to load Currencies");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<Void> deleteCurrency(Long currencyId) {

        log.info("Attempting to delete currency with id {}", currencyId);

        try {
            if (currencyId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Currency id is required",
                        null,
                        LocalDateTime.now()
                );
            }
            Currency currency = currencyRepo.findById(currencyId)
                    .orElseThrow(() ->
                            new RuntimeException("Currency not found with id " + currencyId)
                    );
            if ("INR".equalsIgnoreCase(currency.getCurrencyCode())) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Base currency INR cannot be deleted",
                        null,
                        LocalDateTime.now()
                );
            }
            currencyRepo.delete(currency);
            log.info("Currency deleted successfully with id {}", currencyId);
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Currency deleted successfully",
                    null,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Currency deletion failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while deleting currency", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting currency",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}