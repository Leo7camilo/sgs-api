package com.br.sgs.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;

import com.br.sgs.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDto {

    public interface CompanyView {
        public static interface RegistrationPost {}
        public static interface CompanyPut {}
    }
	
    @NotBlank(groups = CompanyView.RegistrationPost.class)
    @Size(min = 3, max = 50, groups = CompanyView.RegistrationPost.class)
    //@UsernameConstraint(groups = CompanyView.RegistrationPost.class)
    @JsonView(CompanyView.RegistrationPost.class)
    private String name;
     
    @NotBlank(groups = CompanyView.RegistrationPost.class)
    @CNPJ(groups = {CompanyView.RegistrationPost.class, CompanyView.CompanyPut.class})
    @JsonView({CompanyView.RegistrationPost.class, CompanyView.CompanyPut.class})
	private String document;


    
}
