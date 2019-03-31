package ru.dm_ushakov.mycalculator.parser;

import java.io.IOException;
import java.io.StringReader;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import ru.dm_ushakov.mycalculator.lang.operators.Number;
import ru.dm_ushakov.mycalculator.parser.antlr.*;
import ru.dm_ushakov.mycalculator.lang.operators.*;

public final class Parser {
	private Parser() {
		
	}
	
	private static class ExprOperatorVisitor extends ExprBaseVisitor<AbstractOperator> {
		@Override
		public AbstractOperator visitLine(ExprParser.LineContext ctx) {
			return visit(ctx.expr());
		}
		
		@Override
		public AbstractOperator visitNumber(ExprParser.NumberContext ctx) {
			return new Number(ctx.NUM().getText());
		}
		@Override
		public AbstractOperator visitPlusMinus(ExprParser.PlusMinusContext ctx) {
			if(ctx.op.getType() == ExprParser.PLUS) {
				return new PlusOperator(visit(ctx.expr(0)),visit(ctx.expr(1)));
			} else {
				return new MinusOperator(visit(ctx.expr(0)),visit(ctx.expr(1)));
			}
		}
		@Override
		public AbstractOperator visitComparation(ExprParser.ComparationContext ctx) {
			return new ComparationOperator(ctx.op.getType(),visit(ctx.expr(0)),visit(ctx.expr(1)));
		}
		@Override
		public AbstractOperator visitVariable(ExprParser.VariableContext ctx) {
			return new VariableExtractor(ctx.ID().getText());
		}
		@Override
		public AbstractOperator visitNegativeNumber(ExprParser.NegativeNumberContext ctx) {
			return Number.getNegative(ctx.NUM().getText());
		}
		@Override
		public AbstractOperator visitParenthesis(ExprParser.ParenthesisContext ctx) {
			return new Parenthesis(visit(ctx.expr()));
		}
		@Override
		public AbstractOperator visitTernary(ExprParser.TernaryContext ctx) {
			return new TernaryOperator(visit(ctx.expr(0)),visit(ctx.expr(1)),visit(ctx.expr(2)));
		}
		@Override
		public AbstractOperator visitMulDiv(ExprParser.MulDivContext ctx) {
			if(ctx.op.getType() == ExprParser.MUL) {
				return new MultiplyOperator(visit(ctx.expr(0)),visit(ctx.expr(1)));
			} else {
				return new DivideOperator(visit(ctx.expr(0)),visit(ctx.expr(1)));
			}
		}
		@Override
		public AbstractOperator visitAssignVar(ExprParser.AssignVarContext ctx) {
			return new AssignOperator(new VariableExtractor(ctx.ID().getText()),visit(ctx.expr()));
		}
		@Override
		public AbstractOperator visitAssignFunc(ExprParser.AssignFuncContext ctx) {
			return new AssignOperator(visit(ctx.funcPattern()),visit(ctx.expr()));
		}
		@Override
		public AbstractOperator visitFuncPattern(ExprParser.FuncPatternContext ctx) {
			FunctionExecutor func = new FunctionExecutor(ctx.ID(0).getText());
			
			boolean firstId=true;
			for(TerminalNode id:ctx.ID()) {
				if(firstId) {
					firstId=false;
					continue;
				} else {
					func.addChildOperator(new VariableExtractor(id.getText()));
				}
			}
			
			return func;
		}
		@Override
		public AbstractOperator visitFuncCall(ExprParser.FuncCallContext ctx) {
			FunctionExecutor func = new FunctionExecutor(ctx.ID().getText());
			
			for(ExprParser.ExprContext expr:ctx.expr()) {
				func.addChildOperator((AbstractOperator)visit(expr));
			}
			
			return func;
		}
	}
	
	private static CommonTokenStream getTokenStream(String expression) {
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(new StringReader(expression));
		}catch(IOException ex) {
			throw new RuntimeException(ex);
		}
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        return tokens;
	}

	public static AbstractOperator parseExpression(String expression) {
		CommonTokenStream tokens = getTokenStream(expression);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.line();

        ExprOperatorVisitor eval = new ExprOperatorVisitor();
        return (AbstractOperator) eval.visit(tree);
	}
	
	public static void printDebugParseTree(String expression) {
		CommonTokenStream tokens = getTokenStream(expression);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.line();
        
        System.out.println(tree.toStringTree(parser));
	}
}
