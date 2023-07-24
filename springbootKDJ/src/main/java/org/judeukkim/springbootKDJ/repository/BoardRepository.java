package org.judeukkim.springbootKDJ.repository;

import org.judeukkim.springbootKDJ.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board,Long> {

    @Modifying //수정
    @Query("update Board b set b.hit=b.hit+1 where b.id=:id")
    void hitCountUp(Long id);
}
