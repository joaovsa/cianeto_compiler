package ast;
import lexer.Token;

public class CompositeExpr extends Expr{
	Expr left;
    Token op;
    Expr right;

    public CompositeExpr(Expr left, Token op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Type getType() {
        if ((op == Token.EQ) ||
            (op == Token.GE) ||
            (op == Token.GT) ||
            (op == Token.LT) ||
            (op == Token.LE) ||
            (op == Token.NEQ) ||
            (op == Token.AND) ||
            (op == Token.OR))
        {
            return Type.booleanType;
        }

        return Type.intType;
    }

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	}
}
