//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:49 PM
//



//
// In order to convert some functionality to Visual C#, the Java Language Conversion Assistant
// creates "support classes" that duplicate the original functionality.
//
// Support classes replicate the functionality of the original code, but in some cases they are
// substantially different architecturally. Although every effort is made to preserve the
// original architecture of the application in the converted project, the user should be aware that
// the primary goal of these support classes is to replicate functionality, and that at times
// the architecture of the resulting solution may differ somewhat.
//
/**
* This interface should be implemented by any class whose instances are intended
* to be executed by a thread.
*/
public interface IThreadRunnable   
{
    /**
    * This method has to be implemented in order that starting of the thread causes the object's
    * run method to be called in that separately executing thread.
    */
    void run() throws Exception ;

}


