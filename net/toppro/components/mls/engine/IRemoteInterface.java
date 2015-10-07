//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;


/**
* Interface that can be run over the remote AppDomain boundary.
*/
public interface IRemoteInterface   
{
    Object invoke(String lcMethod, Object[] Parameters) throws Exception ;

}


