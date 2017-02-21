/**
 * 
 */
package com.work.fb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.work.fb.domain.AppMessage;

/**
 * @author 115750
 *
 */
@Service
@Transactional
public class AppServiceImpl implements AppService {

	private static final Logger LOG = LoggerFactory.getLogger(AppServiceImpl.class);


	Pattern alphaPattern = Pattern.compile("[A]\\((\\d+)\\)");
	Pattern decimalPattern = Pattern.compile("[F]\\((\\d+)\\.(\\d+)\\)");
	Pattern numericalValuePattern = Pattern.compile("(\\d+)\\.(\\d+)");

	/**
	 * 
	 */
	public AppServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cts.psp.service.AppService#extractRecords(int, java.util.List,
	 * boolean)
	 */
	@Override
	public int extractRecords(int extractId, List<String> records, boolean hasHeader) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String convert(AppMessage message) {

		return "";
	}

	@Override
	public String format(String data, String format) {

		String resultString = "";

		if (format != null && format.startsWith("A(")) {
			Matcher m = alphaPattern.matcher(format);
			boolean b = m.matches();
			if (b) {
				String number = m.group(1);
				resultString = String.format("%0" + (Integer.parseInt(number) - data.length()) + "d%s", 0, data);
			} else {
				resultString = "Format does not match " + format;
			}

		} else {
			resultString = "Format does not match " + format;
		}
		return resultString;
	}

	@Override
	public String format(Double data, String format) {

		String resultString = "";

		if (format != null) {

			if (format.startsWith("F(")) {

				Matcher match = decimalPattern.matcher(format);
				boolean validFormat = match.matches();

				Matcher numMatch = numericalValuePattern.matcher(data + "");

				String firstNumPart = "";
				String secondNumPart = "";

				if (numMatch.matches()) {
					firstNumPart = numMatch.group(1);
					secondNumPart = numMatch.group(2);
				} else {
					resultString = "Provided number does not match " + data;
				}

				if (validFormat && numMatch.matches()) {

					String firstPart = match.group(1);
					String secondPart = match.group(2);

					String formattedValue1 = String.format("%0" + firstPart + "d", Integer.parseInt(firstNumPart));
					System.out.println("Formatted value 1 : " + formattedValue1);

					String tempString = "" + secondNumPart;

					String formattedValue2 = String.format(
							"%d%0" + (Integer.parseInt(secondPart) - tempString.length()) + "d", Integer.parseInt(secondNumPart), 0);

					resultString = formattedValue1 + "." + formattedValue2;

				} else {
					resultString = "Not a valid decimal format " + format;
				}

			} else {
				resultString = "Format not identified : " + format;
			}
		}
		return resultString;
	}

}
