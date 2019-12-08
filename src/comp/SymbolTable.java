/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package comp;
import ast.Type;
import ast.TypeCianetoClass;

import java.util.HashMap;

public class SymbolTable {
	public HashMap<String, String> localTable;
	public HashMap<String, TypeCianetoClass> classTable;
	
	public SymbolTable(){
		classTable = new HashMap<String, TypeCianetoClass>();
		localTable = new HashMap<String, String>();
	}
	
	public void putLocal(String s,String t) {
		this.localTable.put(s, t);
	}
	
	public String getLocal(String s) {
		return this.localTable.get(s);
	}
	public boolean clearLocal() {
		try {
			this.localTable.clear();
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
			
	}
	
	public void putClass(String s,TypeCianetoClass ciaclass) {
		this.classTable.put(s, ciaclass);
	}
	
	public TypeCianetoClass getClass(String s) {		
		return this.classTable.get(s);
	}
	
	public boolean clearClass() {
		try {
			this.classTable.clear();
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
			
	}
}
