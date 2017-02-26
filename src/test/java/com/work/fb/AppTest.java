package com.work.fb;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AppTest {

	public static String[] mNumbers = { "एक", "दोन", "तीन", "चार", "पाच", "सहा", "सात", "आठ", "नऊ", "दहा", "अकरा",
			"बारा", "तेरा", "चौदा", "पंधरा", "सोळा", "सतरा", "अठरा", "एकोणीस", "वीस", "एकवीस", "बावीस", "तेवीस",
			"चोवीस", "पंचवीस", "सव्वीस", "सत्तावीस", "अठ्ठावीस", "एकोणतीस", "तीस", "एकतीस", "बत्तीस", "तेहेतीस",
			"चौतीस", "पस्तीस", "छत्तीस", "सदतीस", "अडतीस", "एकोणचाळीस", "चाळीस", "एक्केचाळीस", "बेचाळीस", "त्रेचाळीस",
			"चव्वेचाळीस", "पंचेचाळीस", "सेहेचाळीस", "सत्तेचाळीस", "अठ्ठेचाळीस", "एकोणपन्नास", "पन्नास", "एक्कावन्न",
			"बावन्न", "त्रेपन्न", "चोपन्न", "पंचावन्न", "छप्पन्न", "सत्तावन्न", "अठ्ठावन्न", "एकोणसाठ", "साठ", "एकसष्ठ",
			"बासष्ठ", "त्रेसष्ठ", "चौसष्ठ", "पासष्ठ", "सहासष्ठ", "सदुसष्ठ", "अडुसष्ठ", "एकोणसत्तर", "सत्तर",
			"एक्काहत्तर", "बाहत्तर", "त्र्याहत्तर", "चौर्‍याहत्तर", "पंच्याहत्तर", "शहात्तर", "सत्याहत्तर",
			"अठ्ठ्याहत्तर", "एकोण ऐंशी", "ऐंशी"

	};

	public static String[] things = {

			"पेन", "पाकिटे", "केळी", "पैसे", "सायकली", " चिकू", "आंबे", "फणस", "काजू", "पेरू", "कागद", "तिकीट",
			" दगड", "पतंग", "चॉकलेट", "पेढे", "लाडू", "खडू", "दिवे"

	};

	public static String[] names = { "नेहा", "रिया", "अंजली", "भारती", "मयुरी", "मयूर", "देवा", "रीशी", "मानव", "उदेश",
			"वीर", "साहिल", "अभिषेक", "गोकुळ", "उदय", "शिवा", "अर्पित", "अरमान", "अभिजित", "सुजाता", "ख़ुशी", "इशा",
			"सावरीया", "साहिल" };

	@Test
	public void testAplphaNumeric() {

		RandomDataGenerator rdg = new RandomDataGenerator();

		System.out.println("<table>");
		for (int i = 0; i < 15; i++) {

			int firstNumber = rdg.nextInt(0, mNumbers.length - 1);
			int secondNumber = rdg.nextInt(0, mNumbers.length - 1);

			int firstPerson = rdg.nextInt(0, names.length - 1);
			int secondPerson = rdg.nextInt(0, names.length - 1);

			int thingNumber = rdg.nextInt(0, things.length - 1);

			// System.out.println(""+thingNumber+":"+firstPerson+":"+firstNumber+":"+
			// secondPerson +":"+secondNumber);
			
			System.out.println(String.format("<tr><td>%s कडे %s %s आहेत व %s कडे  %s %s आहेत. दोघांकडे एकूण किती %s आहेत ? </td><td>.................</td></tr>",
					names[firstPerson], mNumbers[firstNumber], things[thingNumber], names[secondPerson],
					mNumbers[secondNumber], things[thingNumber], things[thingNumber]));
		}
		System.out.println("</table>");

	}

}
