package com.inno.ui;

public class Validator {

    Validator() {
    }

    public static boolean validate(String var, String validators) {
        boolean valid = true;
        if (!validators.contains("required") && var.trim().length() == 0) {
            return true;
        }
        String[] validatorsData = validators.split("\\|", -1);
        for (String data : validatorsData) {
            if (data.trim().length() != 0) {
                String[] validatorData = data.split("\\:", -1);
                switch (validatorData[0]) {
                case "required":
                    if (var.length() == 0 || var.trim().length() == 0) {
                        valid = false;
                    }
                    break;
                case "numeric":
                    if (!isNumeric(var)) {
                        valid = false;
                    }
                    break;
                case "double":
                    if (!isDouble(var)) {
                        valid = false;
                    }
                    break;
                case "integer":
                    if (!isInteger(var)) {
                        valid = false;
                    }
                    break;
                case "max":
                    if (validatorData.length <= 1 || !isInteger(validatorData[1])
                            || var.length() > new Integer(validatorData[1])) {
                        valid = false;
                    }
                    break;
                case "min":
                    if (validatorData.length <= 1 || !isInteger(validatorData[1])
                            || var.length() < new Integer(validatorData[1])) {
                        valid = false;
                    }
                    break;
                default:
                    break;
                }
            }
        }
        return valid;
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