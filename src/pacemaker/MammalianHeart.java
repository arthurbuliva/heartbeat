/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacemaker;

/**
 *
 * @author bulivaa
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface MammalianHeart extends Remote
{
    public String beat(String equation, Map<String, Double> parameters) throws RemoteException;
}
