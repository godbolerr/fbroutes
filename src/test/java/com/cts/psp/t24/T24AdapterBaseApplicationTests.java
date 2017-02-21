package com.cts.psp.t24;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.cts.psp.t24.domain.AppMessage;
import com.work.fb.AppService;
import com.work.fb.FbRoutesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FbRoutesApplication.class})
public class T24AdapterBaseApplicationTests {
	
	@Inject
	AppService service;

	@Test
	public void testAplphaNumeric() {
	
	}


}
