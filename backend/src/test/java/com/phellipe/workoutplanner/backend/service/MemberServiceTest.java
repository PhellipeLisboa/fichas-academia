package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import com.phellipe.workoutplanner.backend.domain.repository.MemberRepository;
import com.phellipe.workoutplanner.backend.dto.member.CreateMemberRequest;
import com.phellipe.workoutplanner.backend.dto.member.MemberResponse;
import com.phellipe.workoutplanner.backend.dto.member.MemberSummaryResponse;
import com.phellipe.workoutplanner.backend.exception.InvalidDataException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService Tests")
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private CreateMemberRequest createRequest;

    @BeforeEach
    void setUp() {

        member = new Member();
        member.setId(1L);
        member.setName("Paulo");
        member.setPublicCode("ABC12345");
        member.setActive(true);

        createRequest = new CreateMemberRequest();
        createRequest.setName("Paulo");
    }

    @Test
    @DisplayName("Should create member successfully")
    void shouldCreateMemberSuccessfully() {

        when(memberRepository.existsByPublicCode(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.createMember(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Paulo");
        assertThat(response.getPublicCode()).isNotNull();
        assertThat(response.getActive()).isTrue();

        verify(memberRepository, times(1)).save(any(Member.class));

    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {

        CreateMemberRequest invalidRequest = new CreateMemberRequest();
        invalidRequest.setName("");

        assertThatThrownBy(() -> memberService.createMember(invalidRequest))
                .isInstanceOf(InvalidDataException.class);

        verify(memberRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find member by ID successfully")
    void shouldFindMemberByIdSuccessfully() {

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Paulo");

    }

    @Test
    @DisplayName("Should throw exception when member not found")
    void shouldThrowExceptionWhenMemberNotFound() {

        when(memberRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById(111L)).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    @DisplayName("Should find member by public code successfully")
    void shouldFindMemberByPublicCodeSuccessfully() {

        when(memberRepository.findByPublicCode("ABC12345")).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findByPublicCode("ABC12345");

        assertThat(response).isNotNull();
        assertThat(response.getPublicCode()).isEqualTo("ABC12345");
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Paulo");

    }

    @Test
    @DisplayName("Should throw exception when public code not found")
    void shouldThrowExceptionWhenPublicCodeNotFound() {

        when(memberRepository.findByPublicCode("ABC11111")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findByPublicCode("ABC11111"))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    @DisplayName("Should inactivate member successfully")
    void shouldInactivateMemberSucessfully() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.inactivateMember(1L);

        assertThat(member.getActive()).isFalse();
        verify(memberRepository).save(any());

    }

    @Test
    @DisplayName("Should throw exception when inactivating non existing member")
    void shouldThrowExceptionWhenInactivateMemberNotFound() {
        when(memberRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.inactivateMember(111L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(memberRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should activate member successfully")
    void shouldActivateMemberSuccessfully() {
        member.setActive(false);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.activateMember(1L);

        assertThat(member.getActive()).isTrue();
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when activating non existing member")
    void shouldThrowExceptionWhenActivateMemberNotFound() {
        when(memberRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.activateMember(111L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(memberRepository, never()).save(any());

    }

    @Test
    @DisplayName("Should find all active members")
    void shouldFindAllActiveMembers() {

        when(memberRepository.findByActiveTrue()).thenReturn(List.of(member));

        List<MemberSummaryResponse> response = memberService.findAllActiveMembers();

        assertThat(response).isNotEmpty();
        assertThat(response).hasSize(1);
        assertThat(response).contains(MemberSummaryResponse.from(member));

    }

    @Test
    @DisplayName("Should generate another public code when duplicated")
    void shouldGenerateAnotherPublicCodeWhenDuplicated() {
        when(memberRepository.existsByPublicCode(anyString())).thenReturn(true).thenReturn(false);
        when(memberRepository.save(any())).thenReturn(member);

        MemberResponse response = memberService.createMember(createRequest);

        assertThat(response).isNotNull();
        verify(memberRepository).save(any());
    }

}
