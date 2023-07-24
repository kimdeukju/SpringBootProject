package org.judeukkim.springbootKDJ.repository;

import org.judeukkim.springbootKDJ.entity.Board;
import org.judeukkim.springbootKDJ.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    List<Reply> findAllByBoardIdOrderByIdDesc(Board boardEntity);

//    List<Reply> findAllByBoardEntityOrderByReplyIdDesc(Board boardEntity);

}
