package com.yuhangma.test.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/1/22
 */
@Component
public class ContractStatusHandlerFactory {

    @Autowired
    private List<StateHandler> handlers;

    public StateHandler getHandler(ContractStatus status) {
        return handlers.stream().filter(h -> h.getStatus() == status).findFirst()
                .orElseThrow(() -> new RuntimeException("no handler found!"));
    }
}
