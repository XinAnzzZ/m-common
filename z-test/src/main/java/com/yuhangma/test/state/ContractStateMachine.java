package com.yuhangma.test.state;

import lombok.Data;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/1/22
 */
@Data
public class ContractStateMachine {

    private StateHandler currentState;

    public ContractStateMachine(ContractStatus status, ContractStatusHandlerFactory handlerFactory) {
        currentState = handlerFactory.getHandler(status);
    }

    public void sign() {
        currentState.sign(this);
    }

    public void effective() {

    }

    public void invalid() {

    }
}
