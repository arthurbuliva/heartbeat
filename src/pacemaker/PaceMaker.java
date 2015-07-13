/**
 * This is the client program.
 *
 * This should most likely be invoked through a web service
 * so that it is transparent to the users
 */
package pacemaker;

import java.rmi.Naming;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Receives requests and distributes them to the agents
 *
 * @author bulivaa <arthur.buliva@unon.org>
 */
public class PaceMaker extends Brain
{

    private static String currentValve;
    private static MammalianHeart heart;

    /**
     * The main entry point of the client application
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        logApplicationMessage("Pulse OK");
        ecg();
    }

    /**
     * The electrocardiogram machine distributes the pulses between the
     * various valves
     * 
     * @throws Exception
     */
    private static void ecg() throws Exception
    {
        /**
         * The various valves that are available.
         *
         * This Map object represents the available servers that may be used
         * to help in solving the equations
         */
        Map<String, String> pacesetter = new HashMap<>();
        pacesetter.put("1", "//10.100.150.43/RmiServer");
        pacesetter.put("2", "//127.0.0.1/RmiServer");
        pacesetter.put("3", "//localhost/RmiServer");
        pacesetter.put("4", "//10.100.150.43/RmiServer");

        Map variables = new HashMap();
        variables.put("x", 13.0);
        variables.put("y", 3.0);

        String eq = "3 * (sin(y) - 2) / (x - 2)";

        /* Solve without splitting the equation
         // Read what valve is open
         //        String currentValve = String.valueOf(Valve.getOpenValve(1, pacesetter.size()));
         //        System.out.println("Current valve: " + currentValve);
         //        MammalianHeart heart = (MammalianHeart) Naming.lookup(pacesetter.get(currentValve));
         //        System.out.println(heart.beat(eq, variables));
         */
        String[] operands =
        {
            "\\*", "/"
        };

        int idx = new Random().nextInt(operands.length);
        String random = (operands[idx]);

        String[] splitEq = eq.split(random, 2); // No splitting more than twice

//        System.out.println(Arrays.asList(splitEq));
        // Evaluate the first part of the equation
        Expression e = new ExpressionBuilder(splitEq[0])
                .variables(variables.keySet())
                .build();
        e.setVariables(variables);

        // Read what valve is open
        currentValve = String.valueOf(Valve.getOpenValve(1, pacesetter.size()));
        heart = (MammalianHeart) Naming.lookup(pacesetter.get(currentValve));

        double firstValue = Double.parseDouble(heart.beat(splitEq[0], variables));
        System.out.println("Solved using valve: " + currentValve);


        double finalValue = firstValue;

        //Evaluate the remainders
        for (int i = 1; i < splitEq.length; i++)
        {
            // Read what valve is open
            currentValve = String.valueOf(Valve.getOpenValve(1, pacesetter.size()));
            heart = (MammalianHeart) Naming.lookup(pacesetter.get(currentValve));

            switch (random)
            {
                case "\\*":
                    finalValue = firstValue * Double.parseDouble(heart.beat(splitEq[i], variables));
                    System.out.println("Solved using valve: " + currentValve);
                    break;
                case "/":
                    finalValue = firstValue / Double.parseDouble(heart.beat(splitEq[i], variables));
                    System.out.println("Solved using valve: " + currentValve);
                    break;
            }
        }

        System.out.println(finalValue);

    }
}
