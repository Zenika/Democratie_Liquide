package com.zenika.liquid.democracy.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
	
	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllAgencys() {

        LOG.info("getAllAgencys");

        List<String> out = new ArrayList<String>();
        out.add("toto");

        return ResponseEntity.status(HttpStatus.OK).body(out);
    }
	
}
