package com.lch.cas.extras.common;

import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public class Utils {

    public static <T> void setExtraHeader(HttpServletResponse response, Page<T> page) {
        response.setHeader("X-Total-Count", String.valueOf(page.getTotalElements()));
        response.setHeader("Access-Control-Expose-Headers", "X-Total-Count,Content-Range");
    }
}
