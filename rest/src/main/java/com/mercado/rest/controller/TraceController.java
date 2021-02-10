package com.mercado.rest.controller;

import com.mercado.rest.dto.traceService.TraceRequest;
import com.mercado.rest.dto.traceService.TraceResponse;
import com.mercado.rest.service.TraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TraceController {

    private final TraceService traceService;

    @RequestMapping(value = "trace", method = RequestMethod.POST,  produces = { "application/json" })
    public TraceResponse trace(@RequestBody TraceRequest traceRequest) throws Exception {

        return traceService.trace(traceRequest);

    }

}
