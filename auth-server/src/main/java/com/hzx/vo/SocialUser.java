package com.hzx.vo;
/**
 * Copyright 2022 bejson.com
 */

import lombok.Data;

/**
 * Auto-generated: 2022-10-12 21:46:44
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class SocialUser {

    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private String scope;
    private long created_at;

}