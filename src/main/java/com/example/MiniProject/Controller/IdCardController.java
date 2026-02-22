package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.Requests.IdCardRequestDto;
import com.example.MiniProject.Dto.Responses.IdCardResponseDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Service.IdCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/idcard")
public class IdCardController {

    private final IdCardService idCardService;



    @PostMapping("/add")
    public ResponseEntity<ApiResponse<IdCardResponseDto>> addIdCard(@RequestParam Long studId,@RequestBody IdCardRequestDto idCardRequestDto){
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

    public ResponseEntity<ApiResponse<IdCardResponseDto>> upadteIdCard(@RequestParam Long studId,@PathVariable Long id,@RequestBody IdCardRequestDto idCardRequestDto){
        ApiResponse<IdCardResponseDto> response=idCardService.updateIdCard(studId,id,idCardRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIdcardById(@PathVariable Long id){

        ApiResponse<Void> response=idCardService.deleteIdCardById(id);
        return new ResponseEntity<>(response,response.getStatus());
    }
}
