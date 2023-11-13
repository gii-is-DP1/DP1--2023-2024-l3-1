package org.springframework.samples.petclinic.dobble.user;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;
import org.springframework.samples.petclinic.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dobble_users")
public class DobbleUser extends BaseEntity {

    @Column(unique = true)
	String username;

	@NotNull
	@Email
	private String email;

	String password;
	
    @NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
    DobbleAuthorities authority; 

    
    @ManyToMany
	@JoinTable(name = "dobble_user_friends", 
        joinColumns = @JoinColumn(name = "dobble_user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id"), uniqueConstraints = {
			@UniqueConstraint(columnNames = { "dobble_user_id", "friend_id" }) } )
    private List<DobbleUser> friends; 
    
	//Icono jugador (Puede no tener foto (es null) de perfil-> Foto predeterminada)
	@Enumerated(EnumType.STRING)
	private Icon icon; 

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

	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST })
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

}
