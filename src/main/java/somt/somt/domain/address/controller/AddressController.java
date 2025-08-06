package somt.somt.domain.address.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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


@Tag(name ="Address API", description = "주소 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddressController {
    private final AddressService addressService;



    @Operation(summary = "주소 목록 조회" , security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주소 조회 성공 ", content = @Content(schema = @Schema(implementation = AddressPageResponse.class )))
    })
    @GetMapping("/user/addresses")
    public ResponseEntity<?> getAddressList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam(name = "page",defaultValue = "0")Integer page,
                                            @RequestParam(name = "size",defaultValue="10")Integer size){
        CustomPageResponse<AddressResponse> response =  addressService.getAddress(customUserDetails, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(response));
    }

    @Operation(summary = "주소 상세 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "주소 상세 조회 성공", content = @Content(schema = @Schema(implementation = AddressDetailsResponse.class)))
    })
    @GetMapping("/user/address/{addressId}")
    public ResponseEntity<?> getAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @PathVariable(name = "addressId") Long addressId){
        AddressResponse addressResponse = addressService.getAddressDetail(customUserDetails,addressId);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(addressResponse));

    }

    @PostMapping("/user/address")
    @Operation(summary = "주소 생성", security = @SecurityRequirement(name ="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "주소 생성 성공 ", content = @Content(schema = @Schema(implementation = AddressCreateAndModifyResponse.class)))
    public ResponseEntity<?> createAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid AddressRequest addressRequest){
        addressService.create(customUserDetails,addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponse.success("address create success"));
    }

    @PutMapping("/user/address/{addressId}")
    @Operation(summary = "주소 수정" , security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200",description = "주소 수정 성공", content = @Content(schema = @Schema(implementation = AddressCreateAndModifyResponse.class)))
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


@Schema(description = "상품 페이지 ")
class AddressPage extends CustomPageResponse<AddressResponse>{};

@Schema(description = "상품 페이지 응답")
class AddressPageResponse extends CustomResponse<AddressPage>{};


@Schema(description = "상품 상세 조회")
class AddressDetailsResponse extends CustomResponse<AddressResponse>{};


@Schema(description = "상품 생성")
class AddressCreateAndModifyResponse extends CustomResponse<String>{};
