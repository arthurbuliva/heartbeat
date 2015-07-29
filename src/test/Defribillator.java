/**
 * Test class used to test various individual heart muscles.
 */
package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * @author Arthur Buliva <arthurbuliva@gmail.com>
 */
public class Defribillator
{

    public static void main(String[] args)
    {

//        String eq = "x + 2";
        String eq = "2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10";

        Map variables = new HashMap();
        variables.put("x", 13.0);
        variables.put("y", 3.0);

        String[] operands =
        {
            "\\*", "/"
        };

        int idx = new Random().nextInt(operands.length);
        String random = (operands[idx]);

        String[] splitEq = eq.split(random, 2);

        System.out.println(Arrays.asList(splitEq));

        // Evaluate the first part of the equation
        Expression e = new ExpressionBuilder(splitEq[0])
                .variables(variables.keySet())
                .build();
        e.setVariables(variables);

        double firstValue = e.evaluate();
        double finalValue = firstValue;

        //Evaluate the remainders
        for (int i = 1; i < splitEq.length; i++)
        {
            e = new ExpressionBuilder(splitEq[i])
                    .variables(variables.keySet())
                    .build();
            e.setVariables(variables);

            switch (random)
            {
                case "\\*":
                    finalValue = firstValue * e.evaluate();
                    break;
                case "/":
                    finalValue = firstValue / e.evaluate();
                    break;
            }

        }

        System.out.println(finalValue);

//        String equation = "3 * sin(y) - 2 / (x - 2)";
//
//        Map variables = new HashMap();
//        variables.put("x", 2.3);
//        variables.put("y", 3.14);
//
//        Expression e = new ExpressionBuilder(equation)
//                .variables(variables.keySet())
//                .build();
//
//        e.setVariables(variables);
//        double result = e.evaluate();
//        System.out.println(result);
    }
}
