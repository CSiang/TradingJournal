package tfip_project.financial_analysis.Security.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Must have this annotation so JPA is aware of this entity.
@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    // Include the "(strategy = GenerationType.IDENTITY)" to avoid generating the roles_seq table.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MUST use "name" but not other terms.
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + "]";
    }

}
