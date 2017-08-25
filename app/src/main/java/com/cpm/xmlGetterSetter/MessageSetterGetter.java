package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class MessageSetterGetter {
	
	ArrayList<String> Message = new ArrayList<String>();
	ArrayList<String> sentDate = new ArrayList<String>();
	
	public ArrayList<String> getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message.add(message);
	}
	public ArrayList<String> getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate.add(sentDate);
	}
	
	

}
