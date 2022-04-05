package com.yuhangma.test.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/1/22
 */
@Getter
@AllArgsConstructor
public enum ContractStatus {

    NOT_SIGN(0, "未签字"),

    NOT_EFFECTIVE(1, "未生效"),

    EFFECTIVE(2, "生效中"),

    EXPIRED(3, "已过期"),
    ;

    private Integer code;

    private String msg;
}
