/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Krakatoa Class
 */
public class TypeCianetoClass extends Type {

   public TypeCianetoClass( String name) {
      super(name);
	  this.name = name;
	  this.fieldList = null;
	  this.publicMethodList = null;
	  this.privateMethodList = null;	  
	  this.superclass = null;
   }

   @Override
   public String getCname() {
      return getName();
   }
   
   public void setFieldList(ArrayList<FieldList> fieldList) {
	      this.fieldList = fieldList;
   }
   
   public void setpublicMethodList(ArrayList<MethodList> publicMethodList) {
	      this.publicMethodList = publicMethodList;
   }
   
   public void setprivateMethodList(ArrayList<MethodList> privateMethodList) {
	      this.privateMethodList = privateMethodList;
   }
   
   public void setSuperClass(TypeCianetoClass superClass, String superclassName) {
      this.superclass = superClass;
      this.superclassName = superclassName;
   }
   public void genJava(PW pw) {
	   	//pw.printIdent("private class " + name);
	   	pw.printIdent("class " + name);
	   	
	   	if ( superclassName != null ) {
	   		pw.print(" extends " + superclassName);
	   	}
	   	
	   	pw.println(" {");
	   	pw.println();
	   	pw.add();

	   	Iterator<MethodList> it1 = privateMethodList.listIterator();
		while(it1.hasNext()){
			it1.next().genJava(pw);
		}
	   
		Iterator<MethodList> it2 = publicMethodList.listIterator();
		while(it2.hasNext()){
			it2.next().genJava(pw);
		}
		
		Iterator<FieldList> it3 = fieldList.listIterator();
		while(it3.hasNext()){
			it3.next().genJava(pw);
		}
	   
		pw.println();
		pw.sub();
		pw.println("}");
		pw.println();
	}

   private String name;
   private String superclassName;
   private TypeCianetoClass superclass;
   private ArrayList<FieldList> fieldList;
   private ArrayList<MethodList> publicMethodList, privateMethodList;
   // 
}
