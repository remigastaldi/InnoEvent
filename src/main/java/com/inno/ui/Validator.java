/*
 * File Created: Wednesday, 21st November 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Thursday, 22nd November 2018
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

    public static boolean isInferiorOf(String var, Integer maxValue, boolean isString) {
        if (isString && var.length() > maxValue) {
            return false;
        } else if (!isString && isNumeric(var)) {
            Integer newVar = new Integer(var);
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
            double d = Double.parseDouble(var);
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("Error cast to double  => " + nfe.getMessage());
            return false;
        }
        return true;
    }

    public static boolean isInteger(String var) {
        try {
            int d = Integer.parseInt(var);
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("Error cast to integer  => " + nfe.getMessage());
            return false;
        }
        return true;
    }

}