/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

/** This class represents a metaobject annotation as <code>{@literal @}ce(...)</code> in <br>
 * <code>
 * @ce(5, "'class' expected") <br>
 * clas Program <br>
 *     public void run() { } <br>
 * end <br>
 * </code>
 *
   @author José

 */
public class MetaobjectAnnotation {

	public MetaobjectAnnotation(String name, ArrayList<Object> paramList) {
		this.name = name;
		this.paramList = paramList;
	}

	public ArrayList<Object> getParamList() {
		return paramList;
	}
	public String getName() {
		return name;
	}
	public void genJava(PW pw) {
		/*pw.printIdent("@");
		pw.print(name);
		
		//Iterator<Object> it = paramList.listIterator();
		int it = 0;
		System.out.println(paramList.size());
		if(paramList.size()>0) {	int flag = 0;
		while(it < paramList.size()){
			flag++;
			System.out.print(it);
			if(flag == 1)
				pw.print("(");
			//it.next().genJava(pw);
			it++;
		}
		if(flag != 0)
			pw.print(")");
		}
		pw.println();*/
	}

	private String name;
	private ArrayList<Object> paramList;

}
