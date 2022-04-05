package com.yuhangma.test.state;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/1/22
 */
public interface StateHandler {

    ContractStatus getStatus();

    void sign(ContractStateMachine contractStateMachine);

    void effective();

    void invalid();
}
