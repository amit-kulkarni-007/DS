package CalculatorApp;


/**
* CalculatorApp/CalculatorPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Calculator.idl
* Sunday, 4 May, 2025 1:07:43 PM IST
*/

public abstract class CalculatorPOA extends org.omg.PortableServer.Servant
 implements CalculatorApp.CalculatorOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("add", new java.lang.Integer (0));
    _methods.put ("subtract", new java.lang.Integer (1));
    _methods.put ("multiply", new java.lang.Integer (2));
    _methods.put ("divide", new java.lang.Integer (3));
    _methods.put ("shutdown", new java.lang.Integer (4));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // CalculatorApp/Calculator/add
       {
         double x = in.read_double ();
         double y = in.read_double ();
         double $result = (double)0;
         $result = this.add (x, y);
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 1:  // CalculatorApp/Calculator/subtract
       {
         double x = in.read_double ();
         double y = in.read_double ();
         double $result = (double)0;
         $result = this.subtract (x, y);
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 2:  // CalculatorApp/Calculator/multiply
       {
         double x = in.read_double ();
         double y = in.read_double ();
         double $result = (double)0;
         $result = this.multiply (x, y);
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 3:  // CalculatorApp/Calculator/divide
       {
         double x = in.read_double ();
         double y = in.read_double ();
         double $result = (double)0;
         $result = this.divide (x, y);
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 4:  // CalculatorApp/Calculator/shutdown
       {
         this.shutdown ();
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:CalculatorApp/Calculator:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Calculator _this() 
  {
    return CalculatorHelper.narrow(
    super._this_object());
  }

  public Calculator _this(org.omg.CORBA.ORB orb) 
  {
    return CalculatorHelper.narrow(
    super._this_object(orb));
  }


} // class CalculatorPOA
