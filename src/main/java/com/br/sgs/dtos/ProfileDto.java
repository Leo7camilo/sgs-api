package com.br.sgs.dtos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.br.sgs.enums.ProfileState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
	
	public interface ProfileView {
	    public static interface RegistrationPost {}
	    public static interface GrantPermission {}
	    public static interface ProfilePut {}
	    public static interface ProfileStatusPut {}
	}
	
	@NotBlank(groups = ProfileView.RegistrationPost.class)
	@Size(min = 4, max = 50, groups = {ProfileView.RegistrationPost.class, ProfileView.ProfilePut.class})
	@JsonView(ProfileView.RegistrationPost.class)
	private String description;
	
	@NotBlank(groups = ProfileView.ProfileStatusPut.class)
	@Size(min = 4, max = 50, groups = ProfileView.ProfileStatusPut.class)
	@JsonView(ProfileView.ProfileStatusPut.class)
	private ProfileState status;
	
	@NotNull(groups = ProfileView.GrantPermission.class)
	@JsonView(ProfileView.GrantPermission.class)
	private Set<UUID> queues = new HashSet<>();
 
}
