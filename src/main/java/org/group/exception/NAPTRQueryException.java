package org.group.exception;

import org.group.dto.ResErrorQuery;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NAPTRQueryException extends RuntimeException {

    private ResErrorQuery resErrorQuery;
    public NAPTRQueryException(ResErrorQuery resErrorQuery) {
        super(resErrorQuery.getErrorMsg());
        this.resErrorQuery = resErrorQuery;
    }

    public ResErrorQuery getResErrorQuery() {
        return resErrorQuery;
    }
}
