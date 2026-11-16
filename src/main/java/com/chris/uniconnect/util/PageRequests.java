package com.chris.uniconnect.util;

import com.chris.uniconnect.Exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

//valida page/size antes de armar el PageRequest: asi un size=0 o page=-1 responde 400 y no 500
public final class PageRequests {

    public static final int MAX_SIZE = 50;

    private PageRequests() {
    }

    //sin orden: para queries que ya traen el ORDER BY en el nombre del metodo (p.ej. ...OrderBySentAtDesc)
    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static PageRequest of(int page, int size, Sort sort) {
        if (page < 0) {
            throw new BadRequestException("El parametro page debe ser mayor o igual a 0");
        }
        if (size < 1 || size > MAX_SIZE) {
            throw new BadRequestException("El parametro size debe estar entre 1 y " + MAX_SIZE);
        }
        return PageRequest.of(page, size, sort);
    }
}
