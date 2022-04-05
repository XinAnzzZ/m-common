package com.yuhangma.test.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/1/22
 */
@Component
public class NotSignStateHandler implements StateHandler {

    @Autowired
    private ContractStatusHandlerFactory handlerFactory;

    @Override
    public ContractStatus getStatus() {
        return ContractStatus.NOT_SIGN;
    }

    @Override
    public void sign(ContractStateMachine contractStateMachine) {
        contractStateMachine.setCurrentState(handlerFactory.getHandler(ContractStatus.EFFECTIVE));
    }

    @Override
    public void effective() {

    }

    @Override
    public void invalid() {

    }
}
