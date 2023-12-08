package org.springframework.samples.petclinic.model.base;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

/**
 * Simple JavaBean domain object with an id property (but as UUID to ensure uniqueness). Used as a base class for
 * objects needing this property.
 */
@MappedSuperclass
public class UUIDEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@NotNull
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
