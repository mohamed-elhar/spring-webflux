package org.group.controller;

import org.group.dto.NAPTRQueryDto;
import org.group.exception.ResErrorCodeEnum;
import org.group.service.NAPTRQueryService;
import org.group.dto.NAPTRRecordData;
import org.group.dto.ResErrorQuery;
import org.group.exception.NAPTRQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class NAPTRQueryController {
    private final NAPTRQueryService naptrQueryService;

    @Autowired
    public NAPTRQueryController(NAPTRQueryService naptrQueryService) {
        this.naptrQueryService = naptrQueryService;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<NAPTRRecordData>> NAPTRQuery(@Validated @RequestBody NAPTRQueryDto naptrQueryDto) {
        Mono<NAPTRRecordData> naptrRecordData = naptrQueryService.prepareNAPTRQuery(naptrQueryDto);
        if(null == naptrRecordData) {
            throw new NAPTRQueryException(new ResErrorQuery(ResErrorCodeEnum.NO_RESPONSE_CODE.getCode(), "No answer"));
        }
        return new ResponseEntity<>(naptrRecordData, HttpStatus.OK);
    }

}
