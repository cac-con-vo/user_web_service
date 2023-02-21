package com.example.user_web_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
	
	private String entityName;
	private String attributeName;
	private String attributeValue;
	public ResourceNotFoundException() {
		super("ResourceNotFoundException occured");
		// TODO Auto-generated constructor stub
	}
	public ResourceNotFoundException(String entityName, String attributeName, String attributeValue) {
		super(entityName +" with "+attributeName+" = "+ attributeValue +" Not found");
		this.entityName = entityName;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	
	
	

}
