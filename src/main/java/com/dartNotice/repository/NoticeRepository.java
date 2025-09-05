package com.dartNotice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dartNotice.model.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByRecptNoIn(List<String> recptNo);
    Page<Notice> findAll(Pageable pageable);
}
