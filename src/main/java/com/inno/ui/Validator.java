package com.inno.ui;

public class Validator {

    Validator() {
    }

    public static boolean validate(String var, String validators) {
        if (!validators.contains("required") && var.trim().length() == 0) {
            return true;
        }
        String[] validatorsData = validators.split("\\|", -1);
        for (int i = 0; i < validatorsData.length; i++) {
            String[] validatorData = validatorsData[i].split("\\:", -1);
            switch (validatorData[0]) {
            case "required":
                if (var.trim().length() == 0) {
                    return false;
                }
                break;
            case "numeric":
                if (!isNumeric(var)) {
                    return false;
                }
                break;
            case "double":
                if (!isDouble(var)) {
                    return false;
                }
                break;
            case "integer":
                if (!isInteger(var)) {
                    return false;
                }
                break;
            case "max":
                if (validatorData.length <= 1 || !isInteger(validatorData[1])
                        || var.length() > new Integer(validatorData[1])) {
                    return false;
                }
                break;
            case "min":
                if (validatorData.length <= 1 || !isInteger(validatorData[1])
                        || var.length() < new Integer(validatorData[1])) {
                    return false;
                }
                break;
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