package com.skhu.moodfriend.app.entity.object_store;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ObjectStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBJECT_ID")
    private Long objectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "OBJECT_NAME", nullable = false)
    private ObjectName objectName;

    @Column(name = "OBJECT_PRICE", nullable = false)
    private Integer price;
}
