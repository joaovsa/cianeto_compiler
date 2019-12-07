/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class PrimaryExpr extends Factor {

	public PrimaryExpr(boolean self, boolean sup, String id1, boolean funcId1,
			String id2,  boolean funcId2, String idColon, ArrayList<Expr> exprList) {
		this.self = self;
		this.sup = sup;
		this.id1 = id1;
		this.funcId1 = funcId1;
		this.id2 = id2;
		this.funcId2 = funcId2;
		this.idColon = idColon;
		this.exprList = exprList;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genJava(PW pw) {
		if ( idColon != null && idColon.charAt(idColon.length() - 1) == ':') {
	        idColon = idColon.substring(0, idColon.length() - 1);
	    }
		if( self == true) {
			pw.print("this");
			if( id1 != null ) {
				pw.print("." + id1);
				if ( funcId1 )
					pw.print("()");
			}
			if ( id2 != null ) {
				pw.print("." + id2);
				if ( funcId2 )
					pw.print("()");
			}
			if( idColon != null ) {
				pw.print("." + idColon + "(");
				Iterator<Expr> it = exprList.listIterator();
				it.next().genJava(pw);
				while(it.hasNext()){
					pw.print(", ");
					it.next().genJava(pw);
				}
				pw.print(")");
			}
		} else if ( sup == true ) {
			pw.print("super");
			if( id1 != null ) {
				pw.print("." + id1);
				if ( funcId1 )
					pw.print("()");
			} else if( idColon != null ) {
				pw.print("." + idColon + "(");
				Iterator<Expr> it = exprList.listIterator();
				it.next().genJava(pw);
				while(it.hasNext()){
					pw.print(", ");
					it.next().genJava(pw);
				}
				pw.print(")");
			}
		} else if ( id1 != null ) {
			pw.print(id1);
			if ( funcId1 )
				pw.print("()");
			if( id2 != null ) {
				pw.print("." + id2);
				if ( funcId2 )
					pw.print("()");
			} else if( idColon != null ) {
				pw.print("." + idColon + "(");
				Iterator<Expr> it = exprList.listIterator();
				it.next().genJava(pw);
				while(it.hasNext()){
					pw.print(", ");
					it.next().genJava(pw);
				}
				pw.print(")");
			}
		}
	}

	private boolean self;
	private boolean sup;
	private String id1;
	private boolean funcId1;
	private String id2;
	private boolean funcId2;
	private String idColon;
	private ArrayList<Expr> exprList;
}
