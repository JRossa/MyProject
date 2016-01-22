package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<LogUser, Long> {

    // Para funcionar a primeira letra passa de minúscula a maiúscula
    public LogUser findByUserNameAndPassword(String userName, String password);

    public LogUser findByUserNameAndRndPassword(String userName, String password);

    public LogUser findByUserName(String userName);
    
    public List <LogUser> findByTeacher(Teacher teacher);
}
