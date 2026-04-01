package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.Requests.CurrencyRequestDto;
import com.example.MiniProject.Dto.Responses.CurrencyResponseDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CurrencyResponseDto>> addCurrency(
            @Valid @RequestBody CurrencyRequestDto currencyRequestDto) {
        ApiResponse<CurrencyResponseDto> response = currencyService.addCurrency(currencyRequestDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CurrencyResponseDto>> updateCurrency(@PathVariable Long id, @Valid @RequestBody
    CurrencyRequestDto currencyRequestDto) {
        ApiResponse<CurrencyResponseDto> response=currencyService.updateCurrency(id,currencyRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<CurrencyResponseDto>>> getAllCurrencies(){
        ApiResponse<List<CurrencyResponseDto>> response=currencyService.getAllCurrencies();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCurrency(@PathVariable Long id){
        ApiResponse<Void> response=currencyService.deleteCurrency(id);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<CurrencyResponseDto>> getCurrencyById(@PathVariable Long id){
        ApiResponse<CurrencyResponseDto> response=currencyService.getCurrencyById(id);
        return new ResponseEntity<>(response,response.getStatus());
    }


}



