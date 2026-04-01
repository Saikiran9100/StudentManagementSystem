package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.BankAccountSummaryDto;
import com.example.MiniProject.Dto.Requests.BankAccountRequestDto;
import com.example.MiniProject.Dto.Responses.BankAccountResponseDto;
import com.example.MiniProject.Dto.StudentSummaryDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bank")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<BankAccountResponseDto>> addBankAccount(@RequestParam Long studId,
                                                                              @RequestParam String currencyCode,
                                                                              @Valid @RequestBody BankAccountRequestDto bankAccountRequestDto){
        ApiResponse<BankAccountResponseDto> response=bankAccountService.addBankAccount(studId,currencyCode,bankAccountRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<BankAccountResponseDto>>> getAllBankAccounts(){
        ApiResponse<List<BankAccountResponseDto>> response=bankAccountService.getAllBankAccounts();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get/{bankId}")
    public ResponseEntity<ApiResponse<BankAccountResponseDto>> getBankAccountById(@PathVariable Long bankId){
        ApiResponse<BankAccountResponseDto> response=bankAccountService.getBankAccountById(bankId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping("/update/{bankId}")
    public ResponseEntity<ApiResponse<BankAccountResponseDto>> updateBankAccount(@PathVariable Long bankId,@RequestParam String currencyCode,@Valid @RequestBody BankAccountRequestDto bankAccountRequestDto){
        ApiResponse<BankAccountResponseDto> response=bankAccountService.updateBankAccount(bankId,currencyCode,bankAccountRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @DeleteMapping("/delete/{bankId}")
     public ResponseEntity<ApiResponse<Void>> deleteBankAccout(@PathVariable Long bankId){
        ApiResponse<Void> response=bankAccountService.deleteBankAccount(bankId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/getpages")
    public ResponseEntity<ApiResponse<PageResponse<BankAccountSummaryDto>>> getBankAccountsWithPagination(
        @RequestParam(required = false,defaultValue = "0") int pageNo,
        @RequestParam(required = false,defaultValue = "5") int pageSize,
        @RequestParam(required = false,defaultValue = "bankId") String sortBy,
        @RequestParam(required = false,defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String bankName,
        @RequestParam(required = false) String branch)
        {
            ApiResponse<PageResponse<BankAccountSummaryDto>> response = bankAccountService.getBankAccountsWithPagination(pageNo-1, pageSize, sortBy, sortDir, bankName, branch);
            return new ResponseEntity<>(response, response.getStatus());

    }


    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<BankAccountResponseDto>>> getAllBankAccountsWithTenants(){
        ApiResponse<List<BankAccountResponseDto>> response=bankAccountService.getAllBankAccountsWithTenants();
        return new ResponseEntity<>(response,response.getStatus());
    }
}
