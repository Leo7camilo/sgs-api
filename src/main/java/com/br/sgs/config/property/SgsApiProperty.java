package com.br.sgs.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("sgs")
@Data
public class SgsApiProperty {
	
	private String allowedOorigin = "http://localhost:4200";
}
