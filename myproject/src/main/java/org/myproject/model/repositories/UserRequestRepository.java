package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.LogUserRequest;
import org.myproject.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRequestRepository extends JpaRepository<LogUserRequest, Long> {

    // Para funcionar a primeira letra passa de minúscula a maiúscula
//    public LogUserRequest findByUserNameAndPassword(String userName, String password);

	@Query(value="SELECT tbl_USER_REQUEST.* "
            + "FROM tbl_USER_REQUEST "
            + "WHERE tbl_USER_REQUEST.REQUEST_TAG=:requestTag", nativeQuery = true)
	public LogUserRequest findByUserRequestTag(@Param("requestTag") String requestTag);
    
}
