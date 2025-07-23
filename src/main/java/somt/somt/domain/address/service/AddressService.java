package somt.somt.domain.address.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.address.dto.AddressRequest;
import somt.somt.domain.address.dto.AddressResponse;
import somt.somt.domain.address.entity.Address;
import somt.somt.domain.address.repository.AddressRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberService memberService;

    @Transactional
    public void create(CustomUserDetails customUserDetails, AddressRequest addressRequest) {
        Member member = memberService.getMember(customUserDetails.getMemberId());

        Address address = new Address(member,addressRequest.getAddress());

        addressRepository.save(address);
    }

    public CustomPageResponse<AddressResponse> getAddress(CustomUserDetails customUserDetails, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);

        Page<Address> newPage = addressRepository.findAllByMemberId(customUserDetails.getMemberId(),pageable);

        List<AddressResponse> addressList  = newPage.stream()
                .map(a->new AddressResponse(a.getId(),a.getAddress())).toList();



        return    CustomPageResponse.of(
                addressList,
                newPage.getTotalPages(),
                newPage.getTotalElements(),
                newPage.getNumber()
        );
    }

    public AddressResponse getAddressDetail(CustomUserDetails customUserDetails, Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ADDRESS));

        if(!address.getMember().getId().equals(customUserDetails.getMemberId())){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }


        return  new AddressResponse(address.getId(),address.getAddress());
    }

    @Transactional
    public void modify(CustomUserDetails customUserDetails, AddressRequest addressRequest, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ADDRESS));

        if(!address.getMember().getId().equals(customUserDetails.getMemberId())){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        address.modify(addressRequest.getAddress());

    }

    @Transactional
    public void delete(CustomUserDetails customUserDetails, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ADDRESS));

        if(!address.getMember().getId().equals(customUserDetails.getMemberId())){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        addressRepository.delete(address);

    }
}
