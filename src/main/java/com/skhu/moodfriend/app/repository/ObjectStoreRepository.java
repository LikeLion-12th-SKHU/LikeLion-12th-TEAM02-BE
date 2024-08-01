package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.store.Object;
import com.skhu.moodfriend.app.domain.store.ObjectStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectStoreRepository extends JpaRepository<ObjectStore, Long> {

    Optional<ObjectStore> findByObject(Object object);
}
