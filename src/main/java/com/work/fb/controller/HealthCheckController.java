package com.work.fb.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.work.fb.Constants;
import com.work.fb.domain.Status;


@RestController
public class HealthCheckController {

    private static final Logger LOG = LoggerFactory.getLogger(HealthCheckController.class);

    @RequestMapping(value = Constants.HEALTH_CHECK_PATH, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> getHealthStatus() {

        LOG.debug("Check application health ...");

        Status status = new Status();
        status.setStatus(Constants.OK);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}