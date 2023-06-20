package tfip_project.financial_analysis.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tfip_project.financial_analysis.Security.Models.AppUser;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    
    final String UPDATE_USER = """
            update app_user
            set email=?, name=?, password=?, username=?
            where id=?;
            """;
    final String GET_USER_BY_EMAIL ="""
            select * from app_user where email = ? ;""";

    @Modifying()
    @Query(value = UPDATE_USER, nativeQuery = true)
    public void updateUser(String email, String name, String password, String username, Long id);

    AppUser findByUsername(String username);

    @Query(value = GET_USER_BY_EMAIL, nativeQuery = true)
    public AppUser findByEmail(String email);

    Boolean existsByUsername(String username);
  
    Boolean existsByEmail(String email);
}
