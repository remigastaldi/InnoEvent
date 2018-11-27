/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.util.ArrayList;

import com.inno.service.pricing.OfferOperationData.LogicalOperator;

public class OfferConditionData {

    protected String _name = new String();
    protected String _description = new String();
    protected LogicalOperator _logicalOperator;
    protected ArrayList<OfferOperation> _offerOperations = new ArrayList<>();

    public OfferConditionData(String name, String description, LogicalOperator logicalOperator) {
        this._name = name;
        this._description = description;
        this._logicalOperator = logicalOperator;
    }

    public String getName() {
        return this._name;
    }

    public String getDescription() {
        return this._description;
    }

    public LogicalOperator getLogicalOperator() {
        return this._logicalOperator;
    }

    public ArrayList<? extends OfferOperationData> getOfferOperations() {
        return this._offerOperations;
    }
}