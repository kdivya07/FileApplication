package com.filemanagement.repositories;

import com.filemanagement.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<Attachment, String> {


}
