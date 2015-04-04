package org.myproject.model.repositories;

import java.util.Date;
import java.util.List;

import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LogSessionRepository extends JpaRepository <LogSession, Long> {
    
    public LogSession findByUser(LogUser user);

    
    @Query(value="SELECT ID "
               + "FROM tbl_SESSION "
               + "WHERE USER_ID = :userId AND ACTIVE = true "
               + "ORDER BY START_DATE DESC "
               + "LIMIT 1", nativeQuery = true)
    public Long findIdByUserAndIdActive(@Param("userId") Long userId);

    
    @Query(value="SELECT tbl_SESSION.* "
            + "FROM tbl_SESSION "
            + "WHERE USER_ID = :userId", nativeQuery = true)
    public List <LogSession> findIdByUser(@Param("userId") Long userId);

    
    @Query(value="SELECT ID "
            + "FROM tbl_SESSION "
            + "WHERE USER_ID = :userId AND ACTIVE = true AND START_DATE = :startDate "
            + "ORDER BY START_DATE DESC "
            + "LIMIT 1", nativeQuery = true)
    public Long findIdByUserAndStartDateAndIdActive(@Param("userId") Long userId, @Param("startDate") Date startDate);


    @Query(value="SELECT tbl_SESSION.* "
            + "FROM tbl_SESSION "
            + "WHERE ACTIVE = true "
            + "ORDER BY START_DATE DESC "
            + "LIMIT 1", nativeQuery = true)
    public List <LogSession> findAllActiveSessions ();
    
    
    @Query(value="DELETE tbl_SESSION.* "
            + "FROM tbl_SESSION "
            + "WHERE ACTIVE = true AND USER_ID = :userId AND ID <> :sessionId ", nativeQuery = true)
    public void deleteAllUserZombiesSessions (@Param("userId") Long userId, @Param("sessionId") Long sessionId);
    
}
