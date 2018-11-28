/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

 package com.inno.service.pricing;

public interface ImmutableOfferOperation {

    public enum RelationalOperator {
        INFERIOR, INFERIOR_OR_EQUALS, SUPERIOR, SUPERIOR_OR_EQUALS, EQUALS, NOT
    }

    public enum LogicalOperator {
        AND, OR
    }

    public String getValue();
    public RelationalOperator getRelationalOperator();
    public LogicalOperator getLogicalOperator();
}