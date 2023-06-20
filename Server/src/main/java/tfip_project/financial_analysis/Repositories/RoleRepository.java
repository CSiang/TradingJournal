package tfip_project.financial_analysis.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tfip_project.financial_analysis.Security.Models.Role;


public interface RoleRepository  extends JpaRepository<Role, Long> {
    Role findByName(String name);

    // Role save(Role role);
}
