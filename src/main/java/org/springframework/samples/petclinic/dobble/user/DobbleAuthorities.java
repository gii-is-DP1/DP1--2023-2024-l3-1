package org.springframework.samples.petclinic.dobble.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dobble_authorities")
public class DobbleAuthorities extends BaseEntity {
	
//	@ManyToOne
//	@JoinColumn(name = "username")
//	User user;
	
//	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	String authority;
	
	
}
