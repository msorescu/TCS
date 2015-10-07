//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:49 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Reflection.Assembly;
import CS2JNet.System.Reflection.BindingFlags;
import CS2JNet.System.StringSupport;
import CS2JNet.System.TypeSupport;
import java.io.File;
import java.util.HashMap;
import net.toppro.components.mls.engine.IRemoteInterface;
import net.toppro.components.mls.engine.RemoteLoaderFactory;

//using System.Collections.Specialized;
//using System.Collections;
/**
* Class that enables running of code dynamcially created at runtime.
* Provides functionality for evaluating and executing compiled code.
*/
public class ScriptEngine   
{
    /**
    * Compiler object used to compile our code
    */
    protected CSharpCodeProvider oCompiler = null;
    /**
    * Reference to the Compiler Parameter object
    */
    protected CompilerParameters oParameters = null;
    /**
    * Reference to the final assembly
    */
    protected Assembly oAssembly = null;
    /**
    * The compiler results object used to figure out errors.
    */
    protected CompilerResults oCompiled = null;
    protected String cOutputAssembly = null;
    protected String cNamespaces = "";
    protected boolean lFirstLoad = true;
    private HashMap<String,String> m_scriptMethodNameList = null;
    /**
    * The object reference to the compiled object available after the first method call.
    * You can use this method to call additional methods on the object.
    * For example, you can use CallMethod and pass multiple methods of code each of
    * which can be executed indirectly by using CallMethod() on this object reference.
    */
    public Object oObjRef = null;
    /**
    * If true saves source code before compiling to the cSourceCode property.
    */
    public boolean lSaveSourceCode = false;
    /**
    * Contains the source code of the entired compiled assembly code.
    * Note: this is not the code passed in, but the full fixed assembly code.
    * Only set if lSaveSourceCode=true.
    */
    public String cSourceCode = "";
    /**
    * Line where the code that runs starts
    */
    protected int nStartCodeLine = 0;
    /**
    * Namespace of the assembly created by the script processor. Determines
    * how the class will be referenced and loaded.
    */
    public String cAssemblyNamespace = "Tcs.Mls";
    /**
    * Name of the class created by the script processor. Script code becomes methods in the class.
    */
    public String cClassName = "ScriptEngine";
    /**
    * Determines if default assemblies are added. System, System.IO, System.Reflection
    */
    public boolean lDefaultAssemblies = true;
    protected AppDomain oAppDomain = null;
    public String cErrorMsg = "";
    public boolean bError = false;
    /**
    * Path for the support assemblies wwScripting and RemoteLoader.
    * By default this can be blank but if you're using this functionality
    * under ASP.Net specify the bin path explicitly. Should include trailing
    * dash.
    */
    //[Description("Path for the support assemblies wwScripting and RemoteLoader. Blank by default. Include trailing dash.")]
    public String cSupportAssemblyPath = "";
    /**
    * The scripting language used. CSharp, VB, JScript
    */
    public String cScriptingLanguage = "CSharp";
    private net.toppro.components.mls.engine.MLSEngine m_mlsEngine;
    /**
    * The language to be used by this scripting class. Currently only C# is supported
    * with VB syntax available but not tested.
    * 
    *  @param lcLanguage CSharp or VB
    */
    public ScriptEngine(String lcLanguage) throws Exception {
        this.setLanguage(lcLanguage);
    }

    public ScriptEngine(net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        this.setLanguage("CSharp");
        m_mlsEngine = mlsEngine;
    }

    /**
    * Specifies the language that is used. Supported languages include
    * CSHARP C# VB
    * 
    *  @param lcLanguage
    */
    public void setLanguage(String lcLanguage) throws Exception {
        this.cScriptingLanguage = lcLanguage;
        if (StringSupport.equals(this.cScriptingLanguage, "CSharp") || StringSupport.equals(this.cScriptingLanguage, "C#"))
        {
            this.oCompiler = (CSharpCodeProvider)CSharpCodeProvider.CreateProvider("CSharp");
            this.cScriptingLanguage = "CSharp";
        }
         
        // else throw(Exception ex);
        this.oParameters = new CompilerParameters();
    }

    /**
    * Adds an assembly to the compiled code
    * 
    *  @param lcAssemblyDll DLL assembly file name
    *  @param lcNamespace Namespace to add if any. Pass null if no namespace is to be added
    */
    public void addAssembly(String lcAssemblyDll, String lcNamespace) throws Exception {
        if (lcAssemblyDll == null && lcNamespace == null)
        {
            // *** clear out assemblies and namespaces
            this.oParameters.ReferencedAssemblies.Clear();
            this.cNamespaces = "";
            return ;
        }
         
        if (lcAssemblyDll != null)
            this.oParameters.ReferencedAssemblies.Add(lcAssemblyDll);
         
        if (lcNamespace != null)
            if (StringSupport.equals(this.cScriptingLanguage, "CSharp"))
                this.cNamespaces = this.cNamespaces + "using " + lcNamespace + ";\r\n";
            else
                this.cNamespaces = this.cNamespaces + "imports " + lcNamespace + "\r\n"; 
         
    }

    /**
    * Adds an assembly to the compiled code.
    * 
    *  @param lcAssemblyDll DLL assembly file name
    */
    public void addAssembly(String lcAssemblyDll) throws Exception {
        this.addAssembly(lcAssemblyDll,null);
    }

    public void addNamespace(String lcNamespace) throws Exception {
        this.addAssembly(null,lcNamespace);
    }

    public void addDefaultAssemblies() throws Exception {
        this.addAssembly("System.dll","System");
    }

    //this.AddNamespace("System.Reflection");
    //this.AddNamespace("System.IO");
    /**
    * Executes a complete method by wrapping it into a class.
    * 
    *  @param lcCode One or more complete methods.
    *  @param lcMethodName Name of the method to call.
    *  @param loParameters any number of variable parameters
    *  @return 
    * Executes a snippet of code. Pass in a variable number of parameters
    * (accessible via the loParameters[0..n] array) and return an object parameter.
    * Code should include:  return (object) SomeValue as the last line or return null
    * 
    *  @param lcCode The code to execute
    *  @param loParameters The parameters to pass the code
    *  @return 
    * Compiles and runs the source code for a complete assembly.
    * 
    *  @param lcSource 
    *  @return
    */
    public boolean compileAssembly(String lcSource, String filePath, String version) throws Exception {
        //this.oParameters.GenerateExecutable = false;
        if (StringSupport.Compare(version, "0", false) == 0)
        {
            this.oParameters.GenerateInMemory = true;
            this.oCompiled = this.oCompiler.CompileAssemblyFromSource(this.oParameters, lcSource);
            if (oCompiled.Errors.HasErrors)
            {
                this.bError = true;
                // *** Create Error String
                this.cErrorMsg = oCompiled.Errors.Count.toString() + " Errors:";
                for (int x = 0;x < oCompiled.Errors.Count;x++)
                    this.cErrorMsg = this.cErrorMsg + "\r\nLine: " + oCompiled.Errors[x].Line.toString() + " - " + oCompiled.Errors[x].ErrorText;
                return false;
            }
             
            this.oAssembly = oCompiled.CompiledAssembly;
        }
        else if (!(new File(filePath)).exists())
        {
            this.cOutputAssembly = filePath;
            this.oParameters.OutputAssembly = this.cOutputAssembly;
            //}
            //else {
            //      this.oParameters.OutputAssembly = this.cOutputAssembly;
            //}
            this.oCompiled = this.oCompiler.CompileAssemblyFromSource(this.oParameters, lcSource);
            if (oCompiled.Errors.HasErrors)
            {
                this.bError = true;
                // *** Create Error String
                this.cErrorMsg = oCompiled.Errors.Count.toString() + " Errors:";
                for (int x = 0;x < oCompiled.Errors.Count;x++)
                    this.cErrorMsg = this.cErrorMsg + "\r\nLine: " + oCompiled.Errors[x].Line.toString() + " - " + oCompiled.Errors[x].ErrorText;
                return false;
            }
             
            this.oAssembly = oCompiled.CompiledAssembly;
        }
        else
            this.oAssembly = Assembly.LoadFile(filePath);  
        return true;
    }

    public HashMap<String,String[]> getMethodInfo(Object loObject) throws Exception {
        MethodInfo[] methodInfos = loObject.getClass().GetMethods(BindingFlags.getStatic() | BindingFlags.getPublic());
        HashMap<String,String[]> result = new HashMap<String,String[]>(methodInfos.length);
        m_scriptMethodNameList = new HashMap<String,String>();
        for (int i = 0;i < methodInfos.length;i++)
        {
            String methodString = "";
            ParameterInfo[] pi = methodInfos[i].GetParameters();
            String[] info = new String[pi.length];
            for (int j = 0;j < pi.length;j++)
            {
                info[j] = pi[j].Name;
            }
            result.Add(methodInfos[i].Name.ToUpper(), info);
            m_scriptMethodNameList.Add(methodInfos[i].Name.ToUpper(), methodInfos[i].Name);
        }
        return result;
    }

    public Object createInstance() throws Exception {
        if (this.oObjRef != null)
        {
            return this.oObjRef;
        }
         
        try
        {
            // *** Create an instance of the new object
            if (this.oAppDomain == null)
                try
                {
                    this.oObjRef = oAssembly.CreateInstance(this.cAssemblyNamespace + "." + this.cClassName);
                    return this.oObjRef;
                }
                catch (Exception ex)
                {
                    this.bError = true;
                    this.cErrorMsg = ex.getMessage();
                    return null;
                }
            
            else
            {
                // create the factory class in the secondary app-domain
                RemoteLoaderFactory factory = (RemoteLoaderFactory)this.oAppDomain.CreateInstance("RemoteLoader", "Westwind.RemoteLoader.RemoteLoaderFactory").Unwrap();
                // with the help of this factory, we can now create a real 'LiveClass' instance
                this.oObjRef = factory.create(this.cOutputAssembly,this.cAssemblyNamespace + "." + this.cClassName,null);
                return this.oObjRef;
            } 
        }
        catch (Exception ex)
        {
            this.bError = true;
            this.cErrorMsg = ex.getMessage();
            return null;
        }
    
    }

    public Object callMethod(Object loObject, String lcMethod, Object... loParameters) throws Exception {
        try
        {
            //m_mlsEngine.WriteLine(lcMethod + ":" + loParameters.ToString());
            // *** Try to run it
            if (m_scriptMethodNameList == null)
                getMethodInfo(this.oObjRef);
             
            lcMethod = m_scriptMethodNameList.get(lcMethod);
            if (this.oAppDomain == null)
                return TypeSupport.InvokeMember(loObject.getClass(), lcMethod, loObject, loParameters);
            else
            {
                // *** Just invoke the method directly through Reflection
                // *** Invoke the method through the Remote interface and the Invoke method
                Object loResult;
                try
                {
                    // *** Cast the object to the remote interface to avoid loading type info
                    IRemoteInterface loRemote = (IRemoteInterface)loObject;
                    // *** Indirectly call the remote interface
                    loResult = loRemote.invoke(lcMethod,loParameters);
                }
                catch (Exception ex)
                {
                    this.bError = true;
                    this.cErrorMsg = ex.getMessage();
                    return null;
                }

                return loResult;
            } 
        }
        catch (Exception ex)
        {
            this.bError = true;
            this.cErrorMsg = ex.getMessage();
        }

        return null;
    }

    public boolean createAppDomain(String lcAppDomain) throws Exception {
        if (lcAppDomain == null)
            lcAppDomain = "TCSscript";
         
        AppDomainSetup loSetup = new AppDomainSetup();
        loSetup.ApplicationBase = AppDomain.CurrentDomain.BaseDirectory;
        this.oAppDomain = AppDomain.CreateDomain(lcAppDomain, null, loSetup);
        return true;
    }

    public boolean unloadAppDomain() throws Exception {
        if (this.oAppDomain != null)
            AppDomain.Unload(this.oAppDomain);
         
        this.oAppDomain = null;
        if (this.cOutputAssembly != null)
        {
            try
            {
                (new File(this.cOutputAssembly)).delete();
            }
            catch (Exception __dummyCatchVar0)
            {
                    ;
            }
        
        }
         
        return true;
    }

    public void release() throws Exception {
        this.oObjRef = null;
    }

    public void dispose() throws Exception {
        this.release();
        this.unloadAppDomain();
    }

    protected void finalize() throws Throwable {
        try
        {
            this.dispose();
        }
        finally
        {
            super.finalize();
        }
    }

}


