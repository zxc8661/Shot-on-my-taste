package somt.somt.domain.address.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.address.dto.AddressRequest;
import somt.somt.domain.address.entity.Address;
import somt.somt.domain.address.repository.AddressRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceUnitTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private MemberService memberService;

    @InjectMocks
    private AddressService addressService;

    @Test
    void create(){
        //given
        CustomUserDetails mockCustomUserDetails =
                new CustomUserDetails(
                        new CustomUserData(
                                1L,
                                "member",
                                "ADMIN",
                                "",
                                "nickname")
                );
        AddressRequest MockAR =
                new AddressRequest("직산");

        Member mockMember = new Member(mockCustomUserDetails);

        when(memberService.getMember(1L))
                .thenReturn(mockMember);
        //when
        addressService.create(mockCustomUserDetails,MockAR);

        //then
        verify(memberService).getMember(1L);
        verify(addressRepository).save(any(Address.class));

    }

    @Test
    void getAddress(){
        //given
        CustomUserDetails mockCustomUserDetails =
                new CustomUserDetails(
                        new CustomUserData(
                                1L,
                                "member",
                                "ADMIN",
                                "",
                                "nickname")
                );
        int page =0;
        int size =30;

        Address address = new Address()

        Page<Product> page = new PageImpl<>(
                Collections.singletonList(mockProduct),
                PageRequest.of(0,1, Sort.by("createAt").descending()),
                1
        );


        when(addressRepository.findAllByMemberId(anyLong(),any(Pageable.class))).thenReturn()
        //when
        //then
    }

    @Test
    void getAddressDetail(){
        //given
        //when
        //then
    }

    @Test
    void modify(){
        //given
        //when
        //then
    }


    @Test
    void delete(){
        //given
        //when
        //then
    }
}
