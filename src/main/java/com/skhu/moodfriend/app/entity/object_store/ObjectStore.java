package com.skhu.moodfriend.app.entity.object_store;

import com.skhu.moodfriend.app.entity.member.object.Object;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ObjectStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBJECT_ID")
    private Long objectId;

    @Column(name = "OBJECT_NAME", nullable = false, length = 100)
    private String objectName;

    @Column(name = "OBJECT_PRICE", nullable = false)
    private int price;

    @OneToMany(mappedBy = "objectStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Object> objects = new ArrayList<>();
}
