package com.cts.psp.t24.domain;

import java.util.Arrays;

public class AppMessage {

	public AppMessage() {
		// TODO Auto-generated constructor stub
	}
	
	String headers;
	
	
	String rules;
	
	
	String[] data;
	
	
	String output;


	/**
	 * @return the headers
	 */
	public String getHeaders() {
		return headers;
	}


	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(String headers) {
		this.headers = headers;
	}


	/**
	 * @return the rules
	 */
	public String getRules() {
		return rules;
	}


	/**
	 * @param rules the rules to set
	 */
	public void setRules(String rules) {
		this.rules = rules;
	}


	/**
	 * @return the data
	 */
	public String[] getData() {
		return data;
	}


	/**
	 * @param data the data to set
	 */
	public void setData(String[] data) {
		this.data = data;
	}


	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}


	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppMessage [" + (headers != null ? "headers=" + headers + ", " : "")
				+ (rules != null ? "rules=" + rules + ", " : "")
				+ (data != null ? "data=" + Arrays.toString(data) + ", " : "")
				+ (output != null ? "output=" + output : "") + "]";
	}
	
}
