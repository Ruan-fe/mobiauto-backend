package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.MemberRole;
import com.mobiautobackend.domain.repositories.MemberRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @PostConstruct
    private void createInitialAdminUser() {
        Member member = new Member();
        member.setEmail("admin@mobiautobackend.com");
        member.setPassword(new BCryptPasswordEncoder().encode("admin"));
        member.setRole(MemberRole.ADMIN);
        this.create(member);
    }

    public boolean isAllowed(String memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()))) {
            return true;
        }
        Member member = (Member) authentication.getPrincipal();
        return Objects.equals(memberId, member.getId());
    }
}
