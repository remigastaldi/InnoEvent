/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Tuesday, 13th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

 package com.inno.service.pricing;

public class OfferOperationData {

    public enum RelationalOperator {
        INFERIOR, INFERIOR_OR_EQUALS, SUPERIOR, SUPERIOR_OR_EQUALS, EQUALS, NOT
    }

    public enum LogicalOperator {
        AND, OR
    }

    protected String _value;
    protected RelationalOperator _relationalOperator;
    protected LogicalOperator _logicalOperator;

    OfferOperationData(String value, RelationalOperator relationalOperator, LogicalOperator logicalOperator) {
        this._value = value;
        this._relationalOperator = relationalOperator;
        this._logicalOperator = logicalOperator;
    }

    public String getValue() {
        return this._value;
    }

    public RelationalOperator getRelationalOperator() {
        return this._relationalOperator;
    }

    public LogicalOperator getLogicalOperator() {
        return this._logicalOperator;
    }
}