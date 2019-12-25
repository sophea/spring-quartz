package com.sma.quartz.security.jtw.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.sma.quartz.utils.Constants.TOKEN_TYPE;


@ApiModel(description = "access token returned by Security API")
@Getter
@Setter
public class AccessToken {

    @ApiModelProperty("JWT token, that would be placed into Authorization header")
    private String accessToken;

    @ApiModelProperty("will return Bearer, the Authorization header type")
    private String tokenType = TOKEN_TYPE;

    @ApiModelProperty("token expiry time, in seconds")
    private int expiresIn;

    public AccessToken(String accessToken, int expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

}
