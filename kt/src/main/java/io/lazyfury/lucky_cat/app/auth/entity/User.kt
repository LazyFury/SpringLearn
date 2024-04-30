package io.lazyfury.lucky_cat.app.auth.entity

import com.alibaba.excel.annotation.ExcelIgnore
import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.write.style.ColumnWidth
import com.alibaba.excel.annotation.write.style.HeadFontStyle
import io.lazyfury.lucky_cat.common.entity.BaseEntity
import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime


@Entity
@ColumnWidth(16)
@HeadFontStyle(fontHeightInPoints = 12)
class User(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelProperty("ID")
    var id: Long,

    @Column
    @ExcelProperty("用户名")
    private var username: String,

    @Column
    private var password: String,

    @Column
    @ExcelProperty("头像")
    var avatar: String = "",

    @Column
    var email: String = "",

    @Column
    var phone: String = "",

    @Column
    var enabled: Boolean = true,

    @Column
    @ExcelProperty("账号未过期")
    var accountNonExpired: Boolean = true,

    @Nullable
    @Column
    var birthdate: LocalDateTime? = null,

    @Column
    var accountNonLocked: Boolean = true,

    @Column
    var lastLogin: String = "",

    @Nullable
    @JoinColumn(referencedColumnName = "id")
    @ManyToOne
    @ExcelIgnore
    var vip: UserVipCard? = null
) : UserDetails, BaseEntity() {
    constructor() : this(0, "", "")
    constructor(id: Long) : this(id, "", "")

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return accountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}