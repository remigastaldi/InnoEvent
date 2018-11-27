/*
 * File Created: Wednesday, 21st November 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Validator {

    Validator() {
    }

    interface ValidatorFunction<T, U, R> {
        R apply(T t, U u);
    }

    public static boolean validate(String var, String validators) {
        if (var == null || validators == null) {
            return false;
        }
        if (validators.contains("required") || var.trim().length() != 0) {
            HashMap<String, ValidatorFunction<String, String, Boolean>> checkers = new LinkedHashMap<>();
            checkers.put("required", (v, n) -> {
                return !(v.trim().length() == 0);
            });
            checkers.put("numeric", (v, n) -> {
                return isNumeric(v);
            });
            checkers.put("double", (v, n) -> {
                return isDouble(v);
            });
            checkers.put("integer", (v, n) -> {
                return isInteger(v);
            });
            checkers.put("max", (v, n) -> {
                if (n == null || !isInteger(n))
                    return false;
                Integer newN = new Integer(n);
                return isInferiorOf(v, newN, !validators.contains("numeric"));
            });
            checkers.put("min", (v, n) -> {
                if (n == null || !isInteger(n))
                    return false;
                Integer newN = new Integer(n);
                return isSuperiorOf(v, newN, !validators.contains("numeric"));
            });

            String[] validatorsTab = validators.split("\\|", -1);
            for (String validator : validatorsTab) {
                String[] validatorTab = validator.split("\\:", -1);
                String validatorData = validatorTab.length <= 1 ? null : validatorTab[1];
                validator = validatorTab[0];

                if (checkers.containsKey(validator) && checkers.get(validator).apply(var, validatorData) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isSuperiorOf(String var, Integer minValue, boolean isString) {
        if (isString && var.length() < minValue) {
            return false;
        } else if (!isString && isDouble(var)) {
            Double newVar = Double.parseDouble(var);
            if (newVar < minValue) {
                return false;
            }
        } else if (!isString && isInteger(var)) {
            Integer newVar = Integer.parseInt(var);
            if (newVar < minValue) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInferiorOf(String var, Integer maxValue, boolean isString) {
        if (isString && var.length() > maxValue) {
            return false;
        } else if (!isString && isDouble(var)) {
            Double newVar = Double.parseDouble(var);
            if (newVar > maxValue) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String var) {
        return var.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isDouble(String var) {
        try {
            Double.parseDouble(var);
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("Error cast to double  => " + nfe.getMessage());
            return false;
        }
        return true;
    }

    public static boolean isInteger(String var) {
        try {
            Integer.parseInt(var);
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("Error cast to integer  => " + nfe.getMessage());
            return false;
        }
        return true;
    }

}