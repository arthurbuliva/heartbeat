/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacemaker;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * @author Arthur Buliva <arthurbuliva@gmail.com>
 */
public class EquationSolver
{

    private static String currentValve;
    private static MammalianHeart heart;
    private static int retries = 0;
    private static final int MAX_RETRIES = 20;

    /**
     * The electrocardiogram machine distributes the pulses between the various
     * valves
     *
     * @param eq The equation to solve
     * @param variables The variables in the equation
     * @return The solution
     */
    public static String solve(String eq, Map variables)
    {
        retries++;

        /**
         * The various valves that are available.
         *
         * This Map object represents the available servers that may be used to
         * help in solving the equations
         */
        Map<String, String> pacesetter = new HashMap<>();
        pacesetter.put("1", "//127.0.0.1/RmiServer");
        pacesetter.put("2", "//127.0.0.1/RmiServer");
        pacesetter.put("3", "//localhost/RmiServer");
        pacesetter.put("4", "//127.0.0.1/RmiServer");
        pacesetter.put("5", "//172.16.27.21/RmiServer");

        try
        {
//        String eq = "3 * (sin(y) - 2) / (x - 2)";

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
            System.out.println("Solved [" + eq + "] using valve: " + currentValve);

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
//                        System.out.println("Solved using valve: " + currentValve);
                        break;
                    case "/":
                        finalValue = firstValue / Double.parseDouble(heart.beat(splitEq[i], variables));
//                        System.out.println("Solved using valve: " + currentValve);
                        break;
                }
            }

            return String.valueOf(finalValue);
        }
        catch (java.rmi.ConnectException ex)
        {
            if (retries == MAX_RETRIES)
            {
                try
                {
                    throw new RemoteException("??\n[Cannot solve equation because no servers are available]");
                }
                catch (RemoteException noServersAvailable)
                {
                    return noServersAvailable.getMessage();
                }
            }
            else
            {
                return solve(eq, variables);
            }
        }
        catch (NotBoundException | MalformedURLException | RemoteException | NumberFormatException ex)
        {
            return ex.getMessage();
        }

    }
}
