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

   public MethodList getPublicMethodList(String name){
       int it;
       for (it=0;it<publicMethodList.size();it++){
           if (publicMethodList.get(it).getName().equals(name)){
               return publicMethodList.get(it);
           }
       }
       return null;
   }
   
   public String getName() {
	   return name;
   }

   public String getSuperclassName() {
	   return superclassName;
   }
   
   private String name;
   private String superclassName;
   private TypeCianetoClass superclass;
   private ArrayList<FieldList> fieldList;
   private ArrayList<MethodList> publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
