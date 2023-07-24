package org.judeukkim.springbootKDJ.repository;

import org.judeukkim.springbootKDJ.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {

}
