package org.springframework.samples.petclinic.dobble.user;

import java.util.List;

import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dobbleusers")
public class User extends BaseEntity {

    @Column(unique = true)
	String username;

	@NotNull
	@Email
	private String email;

	String password;
	
    @NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
    Authorities authority; 

    @NotNull
    @ManyToMany
    private List<User> friends; 
    
	//Icono jugador

    public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}

}
