//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Reflection.BindingFlags;
import net.toppro.components.mls.engine.IRemoteInterface;

/**
* Factory class to create objects exposing IRemoteInterface
*/
public class RemoteLoaderFactory  extends MarshalByRefObject 
{
    private static final BindingFlags bfi = BindingFlags.getInstance() | BindingFlags.getPublic() | BindingFlags.CreateInstance;
    public RemoteLoaderFactory() throws Exception {
    }

    /**
    * Factory method to create an instance of the type whose name is specified,
    * using the named assembly file and the constructor that best matches the specified parameters. 
    *  @param assemblyFile  The name of a file that contains an assembly where the type named typeName is sought. 
    *  @param typeName  The name of the preferred type. 
    *  @param constructArgs  An array of arguments that match in number, order, and type the parameters of the constructor to invoke, or null for default constructor. 
    *  @return  The return value is the created object represented as ILiveInterface.
    */
    public IRemoteInterface create(String assemblyFile, String typeName, Object[] constructArgs) throws Exception {
        return (IRemoteInterface)Activator.CreateInstanceFrom(assemblyFile, typeName, false, bfi, null, constructArgs, null, null).Unwrap();
    }

}


