package com.ur.urcap.examples.userinput.impl;


import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;

public class PasswordValidator implements InputValidator<String> {

	@Override
	public boolean isValid(String value) {
		//At least 1 letter (a-zA-Z), 1 digit and minimum 8 characters long
		return value.matches("^(?=.*[A-Za-z])(?=.*[0-9]).{8,}$");
	}

	@Override
	public String getMessage(String value) {
		return "The password must be at least 8 characters long, contain min. 1 letter and min. 1 number";
	}
}
