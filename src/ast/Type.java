/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import com.sun.jdi.BooleanType;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type undefinedType = new TypeUndefined();
    public static Type nullType = new TypeNull();

    public String getName() {
        return name;
    }
    
    public static String getStringType(Type t) {
    	if(t == Type.booleanType)
    		return "boolean";
    	else if(t == Type.intType)
    		return "int";
    	else if(t == Type.stringType)
    		return "string";
    	else if(t == Type.undefinedType)
    		return "undefined";
    	else if(t == Type.nullType)
    		return "null";
    	return "class or method";
    }
    abstract public String getCname();

    private String name;
}
