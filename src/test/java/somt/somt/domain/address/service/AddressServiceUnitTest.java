//package somt.somt.domain.address.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import somt.somt.common.security.dto.CustomUserData;
//import somt.somt.common.security.dto.CustomUserDetails;
//import somt.somt.domain.address.dto.AddressRequest;
//import somt.somt.domain.address.dto.AddressResponse;
//import somt.somt.domain.address.entity.Address;
//import somt.somt.domain.address.repository.AddressRepository;
//import somt.somt.domain.member.entity.Member;
//import somt.somt.domain.member.service.MemberService;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//public class AddressServiceUnitTest {
//
//    @Mock
//    private AddressRepository addressRepository;
//    @Mock
//    private MemberService memberService;
//
//    @InjectMocks
//    private AddressService addressService;
//
//    @Test
//    void create(){
//        //given
//        CustomUserDetails mockCustomUserDetails =
//                new CustomUserDetails(
//                        new CustomUserData(
//                                1L,
//                                "member",
//                                "ADMIN",
//                                "",
//                                "nickname")
//                );
//        AddressRequest MockAR =
//                new AddressRequest("직산");
//
//        Member mockMember = new Member(mockCustomUserDetails);
//
//        when(memberService.getMember(1L))
//                .thenReturn(mockMember);
//        //when
//        addressService.create(mockCustomUserDetails,MockAR);
//
//        //then
//        verify(memberService).getMember(1L);
//        verify(addressRepository).save(any(Address.class));
//
//    }
//
//    @Test
//    void getAddress(){
//        //given
//        CustomUserDetails mockCustomUserDetails =
//                new CustomUserDetails(
//                        new CustomUserData(
//                                1L,
//                                "member",
//                                "ADMIN",
//                                "",
//                                "nickname")
//                );
//        int page =0;
//        int size =30;
//
//        Member mockMember = new Member();
//
//        Address address = new Address(100L,mockMember,"천안");
//
//
//        Pageable pageable = PageRequest.of(page,size);
//
//        Page<Address> mockPage  = new PageImpl<>(
//                Collections.singletonList(address),
//                pageable,
//                1
//        );
//
//
//
//        when(addressRepository.findAllByMemberId(anyLong(),any(Pageable.class))).thenReturn(mockPage);
//        //when
//        Map<String,Object> mockResult = addressService.getAddress(mockCustomUserDetails,page,size);
//
//        //then
//        /*
//        content 꺼내서 타입 캐스팅
//         */
//        @SuppressWarnings("unchecked")
//        List<AddressResponse> content  = (List<AddressResponse>) mockResult.get("content");
//
//        assertThat(content).hasSize(1);
//        assertThat(content.get(0).getId()).isEqualTo(100L);
//        assertThat(content.get(0).getAddress()).isEqualTo("천안");
//
//        assertThat(mockResult.get("totalPage")).isEqualTo(1);
//        assertThat(mockResult.get("totalElements")).isEqualTo(1L);
//        assertThat(mockResult.get("currentPage")).isEqualTo(0);
//
//    }
//
//
//}
