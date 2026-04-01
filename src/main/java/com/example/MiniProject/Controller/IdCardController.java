package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.IdCardSummaryDto;
import com.example.MiniProject.Dto.Requests.IdCardRequestDto;
import com.example.MiniProject.Dto.Responses.IdCardResponseDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Service.IdCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/idcard")
public class IdCardController {

    private final IdCardService idCardService;



    @PostMapping("/add")
    public ResponseEntity<ApiResponse<IdCardResponseDto>> addIdCard(@RequestParam Long studId,@Valid @RequestBody IdCardRequestDto idCardRequestDto){
        ApiResponse<IdCardResponseDto> response=idCardService.addIdCard(studId,idCardRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }


    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<IdCardResponseDto>>> getAllIdCards(){
        ApiResponse<List<IdCardResponseDto>> response=idCardService.getAllIdCards();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get/{id}")

    public ResponseEntity<ApiResponse<IdCardResponseDto>> getIdCardById(@PathVariable Long id){
        ApiResponse<IdCardResponseDto> response=idCardService.getIdCardById(id);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping("/update/{id}")

    public ResponseEntity<ApiResponse<IdCardResponseDto>> upadteIdCard(@PathVariable Long id, @Valid @RequestBody IdCardRequestDto idCardRequestDto){
        ApiResponse<IdCardResponseDto> response=idCardService.updateIdCard(id,idCardRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIdcard(@PathVariable Long id){

        ApiResponse<Void> response=idCardService.deleteIdCard(id);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/getpages")
    public ResponseEntity<ApiResponse<PageResponse<IdCardSummaryDto>>> getAllIdCards(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate issueDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expiryDate,
            @RequestParam(required = false) Long studentId
    ) {

        ApiResponse<PageResponse<IdCardSummaryDto>> response =
                idCardService.getAllIdCardsWithPagination(
                        pageNo - 1,
                        pageSize,
                        sortBy,
                        sortDir,
                        id,
                        cardNumber,
                        active,
                        issueDate,
                        expiryDate,
                        studentId
                );

        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<IdCardResponseDto>>> getAllIdCardsWithTenants(){
        ApiResponse<List<IdCardResponseDto>> response=idCardService.getAllIdCardsWithTenants();
        return new ResponseEntity<>(response,response.getStatus());
    }
}
