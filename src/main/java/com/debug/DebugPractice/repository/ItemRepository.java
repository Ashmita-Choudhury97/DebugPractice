package com.debug.DebugPractice.repository;

import com.debug.DebugPractice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
