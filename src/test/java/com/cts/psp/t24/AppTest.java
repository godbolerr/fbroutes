package com.cts.psp.t24;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.work.fb.AppService;
import com.work.fb.FbRoutesApplication;
import com.work.fb.domain.AppMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FbRoutesApplication.class})
public class AppTest {
	
	@Inject
	AppService service;

	@Test
	public void testAplphaNumeric() {
	
	}


}
