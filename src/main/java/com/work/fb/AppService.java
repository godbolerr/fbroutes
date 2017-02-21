/**
 * 
 */
package com.work.fb;

import java.util.List;

import com.cts.psp.t24.domain.AppMessage;

/**
 * Responsible for handling File Conversion
 * 
 * @author 115750
 *
 */
public interface AppService {
	public int extractRecords(int extractId, List<String> records, boolean hasHeader);	
	
	public String convert(AppMessage message);
	
	public String format(Double data, String format);
	
	public String format(String data, String format);
	
		
	
}
