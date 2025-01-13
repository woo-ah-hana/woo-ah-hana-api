package org.hana.wooahhanaapi.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    protected UUID id;

    @Column(nullable=false, unique = true)
    protected String username;

    @Column(nullable=false)
    protected String password;

    @Column(nullable=false)
    protected String name;

    @Column(nullable=false)
    protected String phoneNumber;

    @Column(nullable=false)
    protected String accountNumber;

    //@Column(nullable=false)
    protected String accountBank;

    // UserDetail 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorityCollection = new ArrayList<GrantedAuthority>();
        authorityCollection.add(
                (GrantedAuthority) () -> name
        );
        return authorityCollection;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public static MemberEntity create(String username, String name, String password, String phoneNumber, String accountNumber, String accountBank) {
        return new MemberEntity(null,username,password,name,phoneNumber,accountNumber,accountBank);
    }
}
