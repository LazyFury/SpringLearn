package io.lazyfury.mall.code.entity

import jakarta.persistence.*
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails


@Entity
@NoArgsConstructor
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Int?,
    private var username:String,
    @Column(columnDefinition = "varchar(255)")private var password:String,
    var email:String?
):UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return AuthorityUtils.createAuthorityList("user","admin")
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return true
    }

    constructor():this(id = null, username = "", password = "", email = ""){}
}