package wopen.albumserver.domain.model.album;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.NaturalId;
import wopen.albumserver.domain.model.albumcategory.AlbumCategory;
import wopen.albumserver.domain.shared.Audit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    @NaturalId
    @Embedded
    @Column(nullable = false, updatable = false, unique = true)
    private AlbumId albumId;
    @Nationalized
    private String title;
    private String imageUrl;
    private Boolean personal = false;
    @Embedded
    private Audit audit = new Audit();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AlbumCategory> categories = new HashSet<>();
}
