package com.skhu.moodfriend.app.domain.store;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    @Column(name = "OBJECT", nullable = false)
    private Object object;

    @Column(name = "OBJECT_PRICE", nullable = false)
    private int price;

    @Builder
    private ObjectStore(Object object) {
        this.object = object;
        this.price = object.getPrice();
    }
}
