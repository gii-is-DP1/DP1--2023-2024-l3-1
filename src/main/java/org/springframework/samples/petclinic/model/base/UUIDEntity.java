package org.springframework.samples.petclinic.model.base;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
@MappedSuperclass
public class UUIDEntity {
	@Id
	@SequenceGenerator(name = "entity_seq", 
        sequenceName = "entity_sequence")
	@GeneratedValue(strategy = GenerationType.UUID, generator = "entity_seq")
	protected UUID id;

	public UUID getId() {
		return id;
	}

	public UUID setId(UUID id) {
		this.id = id;
		return id;
	}

	@JsonIgnore
	public boolean isNew() {
		return this.id == null;
	}
}
