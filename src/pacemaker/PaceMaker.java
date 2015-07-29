/**
 * This is the client program.
 *
 * This should most likely be invoked through a web service so that it is
 * transparent to the users
 */
package pacemaker;

import java.util.HashMap;
import java.util.Map;

/**
 * Receives requests and distributes them to the agents
 *
 * @author bulivaa <arthur.buliva@unon.org>
 */
public class PaceMaker extends Brain
{

   

    /**
     * The main entry point of the client application
     *
     * @param args
     */
    public static void main(String[] args)
    {
        Map variables = new HashMap();
        variables.put("x", 13.0);
        variables.put("y", 3.0);

        String eq = "(x + y)/(x + y)";

        System.out.println(eq + " = " + EquationSolver.solve(eq, variables));
    }


}