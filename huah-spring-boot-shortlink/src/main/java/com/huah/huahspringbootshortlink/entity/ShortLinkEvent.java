package com.huah.huahspringbootshortlink.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 4123509936908662376L;

    private Long id;
    private String code;
    private String originUrl;
    private Date expireAt;
}