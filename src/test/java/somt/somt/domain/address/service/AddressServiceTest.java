package somt.somt.domain.address.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.address.dto.AddressRequest;
import somt.somt.domain.address.dto.AddressResponse;
import somt.somt.domain.address.entity.Address;
import somt.somt.domain.address.repository.AddressRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AddressRepository addressRepository;



    CustomUserDetails user1;
    CustomUserDetails user2;

    Member member1;
    Member member2;

    @BeforeEach
    void setUp(){
        memberRepository.deleteAll();
        addressRepository.deleteAll();

        member1 = memberRepository.save(Member.create("test1","test1","test1","test1","ADMIN"));
        member2 = memberRepository.save(Member.create("test2","test2","test2","test2","ADMIN"));

       user1 = new CustomUserDetails(new CustomUserData(member1.getId(),member1.getUserName(),member1.getRole(), member1.getPassword(),member1.getNickName()));
       user2 = new CustomUserDetails(new CustomUserData(member2.getId(),member2.getUserName(),member2.getRole(), member2.getPassword(),member2.getNickName()));

    }


    @Test

    void create_and_getAddress() {
        //given
        for(int i=0;i<10;i++){
            if(i%2==0) addressService.create(user2,new AddressRequest("천안 직산읍"+i));
            else  addressService.create(user1,new AddressRequest("천안 직산읍"+i));
        }

        //when
        List<Address> addresses = addressRepository.findAll();


        //then
        assertEquals(addresses.size(),10);
        assertEquals(addresses.get(0).getAddress(),"천안 직산읍0");

    }

    @Test
    void getAddress() {
        //given
        for(int i=0;i<10;i++){
            if(i%2==0) addressService.create(user2,new AddressRequest("천안 직산읍"+i));
            else  addressService.create(user1,new AddressRequest("천안 직산읍"+i));
        }

        //when
        Map<String,Object> addressMap1 = addressService.getAddress(user1,0,10);
        List<AddressResponse> addressList = (List<AddressResponse>) addressMap1.get("content");

        //then
        assertEquals(addressMap1.size(),4);
        assertEquals(addressMap1.get("totalPage"),1);
        assertEquals(addressMap1.get("currentPage"),0);
        assertEquals("천안 직산읍1",addressList.get(0).getAddress());
    }

    @Test
    void getAddressDetail() {
        //given
        for(int i=0;i<10;i++){
            if(i%2==0) addressService.create(user2,new AddressRequest("천안 직산읍"+i));
            else  addressService.create(user1,new AddressRequest("천안 직산읍"+i));
        }
        Long id = addressRepository.findAll().get(0).getId();

        //when
        AddressResponse addressResponse = addressService.getAddressDetail(user2,id);

        assertEquals("천안 직산읍0",addressResponse.getAddress());

    }

    @Test
    @DisplayName("다른 ID 가 address 접근")
    void getAddressDetail_error(){
        for(int i=0;i<10;i++){
            if(i%2==0) addressService.create(user2,new AddressRequest("천안 직산읍"+i));
            else  addressService.create(user1,new AddressRequest("천안 직산읍"+i));
        }

        Long id = addressRepository.findAll().get(0).getId();

        //when

        //then
        CustomException cx = assertThrows(CustomException.class,()->addressService.getAddressDetail(user1,id));
        assertEquals(ErrorCode.ACCESS_DENIED,cx.getErrorCode());
    }

    @Test
    void modify() {
        //given
        for(int i=0;i<10;i++){
            if(i%2==0) addressService.create(user2,new AddressRequest("천안 직산읍"+i));
            else  addressService.create(user1,new AddressRequest("천안 직산읍"+i));
        }
        Long id = addressRepository.findAll().get(0).getId();

        //when
        addressService.modify(user2,new AddressRequest("천안 성환읍"),id);

        CustomException ex = assertThrows(CustomException.class,()->addressService.modify(user1,new AddressRequest("오류"),id));


        assertEquals(ErrorCode.ACCESS_DENIED,ex.getErrorCode());
        assertEquals("천안 성환읍",addressService.getAddressDetail(user2,id).getAddress());
    }

    @Test
    void delete() {
        //given
        for(int i=0;i<10;i++){
            if(i%2==0) addressService.create(user2,new AddressRequest("천안 직산읍"+i));
            else  addressService.create(user1,new AddressRequest("천안 직산읍"+i));
        }
        Long id = addressRepository.findAll().get(0).getId();


        addressService.delete(user2,id);

        assertFalse(addressRepository.existsById(id));
    }
}