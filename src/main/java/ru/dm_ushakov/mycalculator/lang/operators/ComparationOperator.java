package ru.dm_ushakov.mycalculator.lang.operators;

import java.math.BigDecimal;
import ru.dm_ushakov.mycalculator.lang.OperationContext;
import ru.dm_ushakov.mycalculator.parser.antlr.ExprParser;

public class ComparationOperator extends AbstractOperator {
	private enum CompareType{
		Equal(ExprParser.COMP_EQ,"=="),
		MoreThanOrEqual(ExprParser.COMP_GTE,">="),
		LowerThanOrEqual(ExprParser.COMP_LTE,"<="),
		MoreThan(ExprParser.COMP_GT,">"),
		LowerThan(ExprParser.COMP_LT,"<");
		
		private int tokenId;
		private String sign;
		
		public static CompareType solve(int tokenId) {
			for(CompareType ct:CompareType.values()) {
				if(ct.tokenId == tokenId) return ct;
			}
			
			return null;
		}
		
		public String getSign() {
			return sign;
		}
		
		private CompareType(int tokenId,String sign) {
			this.tokenId = tokenId;
			this.sign = sign;
		}
	}
	
	private CompareType compType;
	private AbstractOperator first;
	private AbstractOperator last;
	
	public ComparationOperator(int tokenType,AbstractOperator first,AbstractOperator last) {
		this.compType = CompareType.solve(tokenType);
		this.first = first;
		this.last = last;

		addChildOperator(first);
		addChildOperator(last);
	}

	@Override
	public BigDecimal evalute(OperationContext context) {
		BigDecimal firstNum = first.evalute(context);
		BigDecimal lastNum = last.evalute(context);
		
		boolean result=false;
		switch(compType) {
			case Equal:result=(firstNum.equals(lastNum));break;
			case LowerThan:result=(firstNum.compareTo(lastNum)<0);break;
			case MoreThan:result=(firstNum.compareTo(lastNum)>0);break;
			case LowerThanOrEqual:result=(firstNum.compareTo(lastNum)<=0);break;
			case MoreThanOrEqual:result=(firstNum.compareTo(lastNum)>=0);break;
		}
		
		return result ? BigDecimal.ONE : BigDecimal.ZERO;
	}
	
	@Override
	public String getDebugPayload() {
		return compType.getSign();
	}
}
