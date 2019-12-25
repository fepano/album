package wopen.albumservice.domain.model.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@ToString
@Entity
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    @NaturalId
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private Gender gender = Gender.UNKNOWN;
    @Nationalized
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String avatarUrl;
    @Nationalized
    private String bio;
    private String bannerUrl;
    @Column(nullable = false)
    private Instant registeredAt;

    private Boolean enabled = true;
    private Boolean locked = false;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getLocked() {
        return locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) &&
                email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}