/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Tuesday, 11th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.service.pricing;

public interface ImmutableOfferOperation {

  public enum RelationalOperator {
    INFERIOR, INFERIOR_OR_EQUALS, SUPERIOR, SUPERIOR_OR_EQUALS, EQUALS
  }

  public enum LogicalOperator {
    AND, OR, NOT
  }

  public String getValue();

  public RelationalOperator getRelationalOperator();

  public LogicalOperator getLogicalOperator();

  public ImmutableOfferCondition getParentCondition();

}