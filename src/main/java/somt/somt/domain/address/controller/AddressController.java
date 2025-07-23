package somt.somt.domain.address.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.address.dto.AddressRequest;
import somt.somt.domain.address.dto.AddressResponse;
import somt.somt.domain.address.service.AddressService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/user/addresses")
    public ResponseEntity<?> getAddressList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam(name = "page",defaultValue = "0")Integer page,
                                            @RequestParam(name = "size",defaultValue="10")Integer size){
        CustomPageResponse<AddressResponse> response =  addressService.getAddress(customUserDetails, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(response));
    }

    @GetMapping("/user/address/{addressId}")
    public ResponseEntity<?> getAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @PathVariable(name = "addressId") Long addressId){
        AddressResponse addressResponse = addressService.getAddressDetail(customUserDetails,addressId);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(addressResponse));

    }

    @PostMapping("/user/address")
    public ResponseEntity<?> createAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid AddressRequest addressRequest){
        addressService.create(customUserDetails,addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponse.success("address create success"));
    }

    @PutMapping("/user/address/{addressId}")
    public ResponseEntity<?> modifyAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody AddressRequest addressRequest,
                                           @PathVariable(name = "addressId") Long addressId){
        addressService.modify(customUserDetails,addressRequest,addressId);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success("address modify success"));
    }


    @DeleteMapping("/user/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable(name = "addressId")Long addressId){
        addressService.delete(customUserDetails,addressId);
        return ResponseEntity.status(HttpStatus.OK).body("address delete success");
    }
}
