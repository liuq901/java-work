package parser;

import java.util.*;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.ParserRuleContext;

import parser.tigerParser.*;

import ast.*;

public class tigerLoader extends tigerBaseVisitor<Object> {
	void error(int rule) {
		System.out.println(tigerParser.ruleNames[rule]);
	}

	boolean isLeaf(ParseTree tree) {
		return (tree.getChildCount() == 0);
	}

	char change(char ch) {
		switch (ch) {
		case 'a':
			return ((char) 7);
		case 'b':
			return ((char) 8);
		case 'f':
			return ((char) 12);
		case 'n':
			return ((char) 10);
		case 'r':
			return ((char) 13);
		case 't':
			return ((char) 9);
		case 'v':
			return ((char) 11);
		case '\\':
			return ((char) 92);
		case '\'':
			return ((char) 39);
		case '\"':
			return ((char) 34);
		case '0':
			return ((char) 0);
		default:
			return (ch);
		}
	}

	@Override
	public Program visitProgram(ProgramContext ctx) {
		Program ret = new Program();
		DeclList declList = new DeclList();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_declaration)
				declList.update(visitDeclaration((DeclarationContext) now));
			else if (now.getRuleIndex() == tigerParser.RULE_function_definition) {
				ret.global.add(declList);
				ret.global
						.add(visitFunction_definition((Function_definitionContext) now));
				declList = new DeclList();
			} else
				error(tigerParser.RULE_program);
		}
		ret.global.add(declList);
		return (ret);
	}

	@Override
	public Func visitFunction_definition(Function_definitionContext ctx) {
		Func ret = new Func();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_type_specifier)
				ret.type = visitType_specifier((Type_specifierContext) now);
			else if (now.getRuleIndex() == tigerParser.RULE_plain_declarator) {
				Decl temp = visitPlain_declarator((Plain_declaratorContext) now);
				for (int j = 0; j < temp.pointer; j++)
					ret.type = new Pointer(ret.type);
				ret.name = temp.name;
			} else if (now.getRuleIndex() == tigerParser.RULE_parameters)
				ret.arg = visitParameters((ParametersContext) now);
			else if (now.getRuleIndex() == tigerParser.RULE_compound_statement)
				ret.block = visitCompound_statement((Compound_statementContext) now);
			else
				error(tigerParser.RULE_function_definition);
		}
		return (ret);
	}

	@Override
	public Type visitType_specifier(Type_specifierContext ctx) {
		Type ret = new Type();
		if (ctx.getChildCount() == 1) {
			if (isLeaf(ctx.getChild(0))) {
				TerminalNode now = (TerminalNode) ctx.getChild(0);
				if (now.getSymbol().getType() == tigerParser.Void)
					ret.name = Type.TypeName.Void;
				else if (now.getSymbol().getType() == tigerParser.Char)
					ret.name = Type.TypeName.Char;
				else if (now.getSymbol().getType() == tigerParser.Int)
					ret.name = Type.TypeName.Int;
				else
					error(tigerParser.RULE_type_specifier);
			} else {
				ParserRuleContext now = (ParserRuleContext) ctx.getChild(0);
				if (now.getRuleIndex() == tigerParser.RULE_typedef_name)
					ret = new Alias(
							visitTypedef_name((Typedef_nameContext) now));
				else
					error(tigerParser.RULE_type_specifier);
			}
		} else {
			StructUnion new_ret = new StructUnion();
			new_ret.name = visitStruct_or_union((Struct_or_unionContext) ctx
					.getChild(0));
			if (ctx.getChildCount() == 2)
				new_ret.alias = visitIdentifier((IdentifierContext) ctx
						.getChild(1));
			else {
				new_ret.body = new DeclList();
				Type prev = new Type();
				for (int i = 1; i < ctx.getChildCount(); i++) {
					if (isLeaf(ctx.getChild(i)))
						continue;
					ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
					if (now.getRuleIndex() == tigerParser.RULE_identifier)
						new_ret.alias = visitIdentifier((IdentifierContext) now);
					else if (now.getRuleIndex() == tigerParser.RULE_type_specifier)
						prev = visitType_specifier((Type_specifierContext) now);
					else if (now.getRuleIndex() == tigerParser.RULE_declarators) {
						List<Decl> temp = visitDeclarators((DeclaratorsContext) now);
						new_ret.body.DeclUpdate(prev, temp);
					} else
						error(tigerParser.RULE_type_specifier);
				}
			}
			ret = new_ret;
		}
		return (ret);
	}

	@Override
	public Type.TypeName visitStruct_or_union(Struct_or_unionContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		if (now.getSymbol().getType() == tigerParser.Struct)
			return (Type.TypeName.Struct);
		else if (now.getSymbol().getType() == tigerParser.Union)
			return (Type.TypeName.Union);
		return (null);
	}

	@Override
	public Id visitIdentifier(IdentifierContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		if (now.getSymbol().getType() == tigerParser.ID)
			return (new Id(now.getSymbol().getText()));
		return (null);
	}

	@Override
	public Id visitTypedef_name(Typedef_nameContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		if (now.getSymbol().getType() == tigerParser.ID)
			return (new Id(now.getSymbol().getText()));
		return (null);
	}

	@Override
	public List<Decl> visitDeclarators(DeclaratorsContext ctx) {
		List<Decl> ret = new ArrayList<Decl>();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_declarator)
				ret.add(visitDeclarator((DeclaratorContext) now));
			else
				error(tigerParser.RULE_declarators);
		}
		return (ret);
	}

	@Override
	public Decl visitDeclarator(DeclaratorContext ctx) {
		Decl ret = visitPlain_declarator((Plain_declaratorContext) ctx
				.getChild(0));
		boolean isFunc = false;
		Arg para = new Arg();
		List<Expr> array = new ArrayList<Expr>();
		for (int i = 1; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i))) {
				TerminalNode now = (TerminalNode) ctx.getChild(i);
				if (now.getSymbol().getText().equals("("))
					isFunc = true;
				continue;
			}
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_parameters)
				para = visitParameters((ParametersContext) now);
			else if (now.getRuleIndex() == tigerParser.RULE_constant_expression)
				array.add(visitConstant_expression((Constant_expressionContext) now));
			else
				error(tigerParser.RULE_declarator);
		}
		if (isFunc)
			return (new DeclFunc(ret, para));
		else if (!array.isEmpty())
			return (new DeclArray(ret, array));
		return (ret);
	}

	@Override
	public DeclList visitDeclaration(DeclarationContext ctx) {
		DeclList ret = new DeclList();
		if (isLeaf(ctx.getChild(0))) {
			Type type = new Define(
					visitType_specifier((Type_specifierContext) ctx.getChild(1)));
			List<Decl> decl = visitDeclarators((DeclaratorsContext) ctx
					.getChild(2));
			ret.DeclUpdate(type, decl);
		} else {
			Type type = visitType_specifier((Type_specifierContext) ctx
					.getChild(0));
			List<DeclInit> decl = new ArrayList<DeclInit>();
			if (ctx.getChildCount() == 3)
				decl = visitInit_declarators((Init_declaratorsContext) ctx
						.getChild(1));
			else
				decl.add(new DeclInit());
			ret.InitUpdate(type, decl);
		}
		return (ret);
	}

	@Override
	public List<DeclInit> visitInit_declarators(Init_declaratorsContext ctx) {
		List<DeclInit> ret = new ArrayList<DeclInit>();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_init_declarator)
				ret.add(visitInit_declarator((Init_declaratorContext) now));
			else
				error(tigerParser.RULE_init_declarators);
		}
		return (ret);
	}

	@Override
	public DeclInit visitInit_declarator(Init_declaratorContext ctx) {
		DeclInit ret = new DeclInit();
		ret.decl = visitDeclarator((DeclaratorContext) ctx.getChild(0));
		if (ctx.getChildCount() == 3)
			ret.init = visitInitializer((InitializerContext) ctx.getChild(2));
		return (ret);
	}

	@Override
	public Init visitInitializer(InitializerContext ctx) {
		if (isLeaf(ctx.getChild(0))) {
			Init ret = new Init();
			for (int i = 1; i < ctx.getChildCount(); i++) {
				if (isLeaf(ctx.getChild(i)))
					continue;
				ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
				if (now.getRuleIndex() == tigerParser.RULE_initializer)
					ret.son.add(visitInitializer((InitializerContext) now));
				else
					error(tigerParser.RULE_initializer);
			}
			return (ret);
		} else
			return (new Terminal(
					visitAssignment_expression((Assignment_expressionContext) ctx
							.getChild(0))));
	}

	@Override
	public Arg visitParameters(ParametersContext ctx) {
		Arg ret = new Arg();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i))) {
				TerminalNode now = (TerminalNode) ctx.getChild(i);
				if (now.getSymbol().getText().equals("..."))
					ret.infinity = true;
				continue;
			}
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_plain_declaration) {
				Var var = visitPlain_declaration((Plain_declarationContext) now);
				ret.list.add(var);
			} else
				error(tigerParser.RULE_parameters);
		}
		return (ret);
	}

	@Override
	public Block visitCompound_statement(Compound_statementContext ctx) {
		Block ret = new Block();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_declaration)
				ret.decl.update(visitDeclaration((DeclarationContext) now));
			else if (now.getRuleIndex() == tigerParser.RULE_statement)
				ret.stat.add(visitStatement((StatementContext) now));
			else
				error(tigerParser.RULE_compound_statement);
		}
		return (ret);
	}

	@Override
	public Var visitPlain_declaration(Plain_declarationContext ctx) {
		Type type = visitType_specifier((Type_specifierContext) ctx.getChild(0));
		Decl decl = visitDeclarator((DeclaratorContext) ctx.getChild(1));
		for (int i = 0; i < decl.pointer; i++)
			type = new Pointer(type);
		if (decl.kind == Node.Kind.DeclFunc)
			type = new Function(type, ((DeclFunc) decl).arg);
		else if (decl.kind == Node.Kind.DeclArray) {
			DeclArray temp = (DeclArray) decl;
			for (Expr j : temp.array)
				type = new Array(type, j);
		}
		return (new Var(type, decl.name, new Init()));
	}

	@Override
	public Decl visitPlain_declarator(Plain_declaratorContext ctx) {
		int point = 0;
		Id id = new Id();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i))) {
				if (ctx.getChild(i).getText().equals("*"))
					point++;
				else
					error(tigerParser.RULE_plain_declarator);
				continue;
			}
			ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
			if (now.getRuleIndex() == tigerParser.RULE_identifier)
				id = visitIdentifier((IdentifierContext) now);
			else
				error(tigerParser.RULE_plain_declarator);
		}
		return (new Decl(point, id));
	}

	@Override
	public Stat visitStatement(StatementContext ctx) {
		ParserRuleContext now = (ParserRuleContext) ctx.getChild(0);
		if (now.getRuleIndex() == tigerParser.RULE_expression_statement)
			return (visitExpression_statement((Expression_statementContext) now));
		else if (now.getRuleIndex() == tigerParser.RULE_compound_statement)
			return (visitCompound_statement((Compound_statementContext) now));
		else if (now.getRuleIndex() == tigerParser.RULE_selection_statement)
			return (visitSelection_statement((Selection_statementContext) now));
		else if (now.getRuleIndex() == tigerParser.RULE_iteration_statement)
			return (visitIteration_statement((Iteration_statementContext) now));
		else if (now.getRuleIndex() == tigerParser.RULE_jump_statement)
			return (visitJump_statement((Jump_statementContext) now));
		else
			error(tigerParser.RULE_statement);
		return (null);
	}

	@Override
	public Jumps visitJump_statement(Jump_statementContext ctx) {
		Jumps ret = new Jumps();
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		if (now.getSymbol().getType() == tigerParser.Break)
			ret.type = Jumps.JumpType.Break;
		else if (now.getSymbol().getType() == tigerParser.Continue)
			ret.type = Jumps.JumpType.Continue;
		else if (now.getSymbol().getType() == tigerParser.Return) {
			ret.type = Jumps.JumpType.Return;
			if (!isLeaf(ctx.getChild(1)))
				ret.expr = visitExpression((ExpressionContext) ctx.getChild(1));
		}
		return (ret);
	}

	@Override
	public If visitSelection_statement(Selection_statementContext ctx) {
		If ret = new If();
		ret.expr = visitExpression((ExpressionContext) ctx.getChild(2));
		ret.Then = visitStatement((StatementContext) ctx.getChild(4));
		if (ctx.getChildCount() == 7)
			ret.Else = visitStatement((StatementContext) ctx.getChild(6));
		return (ret);
	}

	@Override
	public For visitIteration_statement(Iteration_statementContext ctx) {
		For ret = new For();
		TerminalNode temp = (TerminalNode) ctx.getChild(0);
		if (temp.getSymbol().getType() == tigerParser.While) {
			ret.expr = visitExpression((ExpressionContext) ctx.getChild(2));
			ret.stat = visitStatement((StatementContext) ctx.getChild(4));
		} else if (temp.getSymbol().getType() == tigerParser.For) {
			int count = 0;
			for (int i = 1; i < ctx.getChildCount(); i++) {
				if (isLeaf(ctx.getChild(i))) {
					TerminalNode now = (TerminalNode) ctx.getChild(i);
					if (now.getSymbol().getText().equals(";"))
						count++;
					continue;
				}
				ParserRuleContext now = (ParserRuleContext) ctx.getChild(i);
				if (now.getRuleIndex() == tigerParser.RULE_expression) {
					Expr expr = visitExpression((ExpressionContext) now);
					if (count == 0)
						ret.init = expr;
					else if (count == 1)
						ret.expr = expr;
					else if (count == 2)
						ret.iter = expr;
					else
						error(tigerParser.RULE_iteration_statement);
				} else if (now.getRuleIndex() == tigerParser.RULE_statement)
					ret.stat = visitStatement((StatementContext) now);
				else
					error(tigerParser.RULE_iteration_statement);
			}
		} else
			error(tigerParser.RULE_iteration_statement);
		return (ret);
	}

	@Override
	public Stat visitExpression_statement(Expression_statementContext ctx) {
		if (isLeaf(ctx.getChild(0)))
			return (new Stat());
		return (visitExpression((ExpressionContext) ctx.getChild(0)));
	}

	@Override
	public Expr visitExpression(ExpressionContext ctx) {
		Expr ret = new Expr();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ret.expr.add(visitAssignment_expression((Assignment_expressionContext) ctx
					.getChild(i)));
		}
		return (ret);
	}

	@Override
	public Expr visitAssignment_expression(Assignment_expressionContext ctx) {
		if (ctx.getChildCount() == 1)
			return (visitLogical_or_expression((Logical_or_expressionContext) ctx
					.getChild(0)));
		Op ret = new Op();
		ret.left = visitUnary_expression((Unary_expressionContext) ctx
				.getChild(0));
		ret.op = visitAssignment_operator((Assignment_operatorContext) ctx
				.getChild(1));
		ret.right = visitAssignment_expression((Assignment_expressionContext) ctx
				.getChild(2));
		return (ret);
	}

	@Override
	public Op.OpType visitAssignment_operator(Assignment_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("="))
			return (Op.OpType.Assign);
		else if (op.equals("+="))
			return (Op.OpType.AddAss);
		else if (op.equals("-="))
			return (Op.OpType.SubAss);
		else if (op.equals("*="))
			return (Op.OpType.MulAss);
		else if (op.equals("/="))
			return (Op.OpType.DivAss);
		else if (op.equals("%="))
			return (Op.OpType.ModAss);
		else if (op.equals("<<="))
			return (Op.OpType.LShiftAss);
		else if (op.equals(">>="))
			return (Op.OpType.RShiftAss);
		else if (op.equals("&="))
			return (Op.OpType.BitAndAss);
		else if (op.equals("^="))
			return (Op.OpType.BitXorAss);
		else if (op.equals("|="))
			return (Op.OpType.BitOrAss);
		else
			error(tigerParser.RULE_assignment_operator);
		return (null);
	}

	@Override
	public Expr visitConstant_expression(Constant_expressionContext ctx) {
		return (visitLogical_or_expression((Logical_or_expressionContext) ctx
				.getChild(0)));
	}

	@Override
	public Expr visitLogical_or_expression(Logical_or_expressionContext ctx) {
		Expr ret = visitLogical_and_expression((Logical_and_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Expr now = visitLogical_and_expression((Logical_and_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, Op.OpType.LogOr, now);
		}
		return (ret);
	}

	@Override
	public Expr visitLogical_and_expression(Logical_and_expressionContext ctx) {
		Expr ret = visitInclusive_or_expression((Inclusive_or_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Expr now = visitInclusive_or_expression((Inclusive_or_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, Op.OpType.LogAnd, now);
		}
		return (ret);
	}

	@Override
	public Expr visitInclusive_or_expression(Inclusive_or_expressionContext ctx) {
		Expr ret = visitExclusive_or_expression((Exclusive_or_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Expr now = visitExclusive_or_expression((Exclusive_or_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, Op.OpType.BitOr, now);
		}
		return (ret);
	}

	@Override
	public Expr visitExclusive_or_expression(Exclusive_or_expressionContext ctx) {
		Expr ret = visitAnd_expression((And_expressionContext) ctx.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Expr now = visitAnd_expression((And_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, Op.OpType.BitXor, now);
		}
		return (ret);
	}

	@Override
	public Expr visitAnd_expression(And_expressionContext ctx) {
		Expr ret = visitEquality_expression((Equality_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Expr now = visitEquality_expression((Equality_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, Op.OpType.BitAnd, now);
		}
		return (ret);
	}

	@Override
	public Expr visitEquality_expression(Equality_expressionContext ctx) {
		Expr ret = visitRelational_expression((Relational_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Op.OpType op = visitEquality_operator((Equality_operatorContext) ctx
					.getChild(i - 1));
			Expr now = visitRelational_expression((Relational_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, op, now);
		}
		return (ret);
	}

	@Override
	public Op.OpType visitEquality_operator(Equality_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("=="))
			return (Op.OpType.Equal);
		else if (op.equals("!="))
			return (Op.OpType.NotEqual);
		else
			error(tigerParser.RULE_equality_operator);
		return (null);
	}

	@Override
	public Expr visitRelational_expression(Relational_expressionContext ctx) {
		Expr ret = visitShift_expression((Shift_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Op.OpType op = visitRelational_operator((Relational_operatorContext) ctx
					.getChild(i - 1));
			Expr now = visitShift_expression((Shift_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, op, now);
		}
		return (ret);
	}

	@Override
	public Op.OpType visitRelational_operator(Relational_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("<"))
			return (Op.OpType.Less);
		else if (op.equals(">"))
			return (Op.OpType.Greater);
		else if (op.equals("<="))
			return (Op.OpType.LEqual);
		else if (op.equals(">="))
			return (Op.OpType.GEqual);
		else
			error(tigerParser.RULE_relational_operator);
		return (null);
	}

	@Override
	public Expr visitShift_expression(Shift_expressionContext ctx) {
		Expr ret = visitAdditive_expression((Additive_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Op.OpType op = visitShift_operator((Shift_operatorContext) ctx
					.getChild(i - 1));
			Expr now = visitAdditive_expression((Additive_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, op, now);
		}
		return (ret);
	}

	@Override
	public Op.OpType visitShift_operator(Shift_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("<<"))
			return (Op.OpType.LShift);
		else if (op.equals(">>"))
			return (Op.OpType.RShift);
		else
			error(tigerParser.RULE_shift_operator);
		return (null);
	}

	@Override
	public Expr visitAdditive_expression(Additive_expressionContext ctx) {
		Expr ret = visitMultiplicative_expression((Multiplicative_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Op.OpType op = visitAdditive_operator((Additive_operatorContext) ctx
					.getChild(i - 1));
			Expr now = visitMultiplicative_expression((Multiplicative_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, op, now);
		}
		return (ret);
	}

	@Override
	public Op.OpType visitAdditive_operator(Additive_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("+"))
			return (Op.OpType.Add);
		else if (op.equals("-"))
			return (Op.OpType.Sub);
		else
			error(tigerParser.RULE_additive_operator);
		return (null);
	}

	@Override
	public Expr visitMultiplicative_expression(
			Multiplicative_expressionContext ctx) {
		Expr ret = visitCast_expression((Cast_expressionContext) ctx
				.getChild(0));
		for (int i = 2; i < ctx.getChildCount(); i += 2) {
			Op.OpType op = visitMultiplicative_operator((Multiplicative_operatorContext) ctx
					.getChild(i - 1));
			Expr now = visitCast_expression((Cast_expressionContext) ctx
					.getChild(i));
			ret = new Op(ret, op, now);
		}
		return (ret);
	}

	@Override
	public Op.OpType visitMultiplicative_operator(
			Multiplicative_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("*"))
			return (Op.OpType.Mul);
		else if (op.equals("/"))
			return (Op.OpType.Div);
		else if (op.equals("%"))
			return (Op.OpType.Mod);
		else
			error(tigerParser.RULE_multiplicative_operator);
		return (null);
	}

	@Override
	public Expr visitCast_expression(Cast_expressionContext ctx) {
		if (isLeaf(ctx.getChild(0))) {
			Type type = visitType_name((Type_nameContext) ctx.getChild(1));
			Expr expr = visitCast_expression((Cast_expressionContext) ctx
					.getChild(3));
			return (new Transform(expr, type));
		} else
			return (visitUnary_expression((Unary_expressionContext) ctx
					.getChild(0)));
	}

	@Override
	public Type visitType_name(Type_nameContext ctx) {
		Type ret = visitType_specifier((Type_specifierContext) ctx.getChild(0));
		for (int i = 1; i < ctx.getChildCount(); i++) {
			TerminalNode now = (TerminalNode) ctx.getChild(i);
			if (now.getSymbol().getText().equals("*"))
				ret = new Pointer(ret);
			else
				error(tigerParser.RULE_type_name);
		}
		return (ret);
	}

	@Override
	public Expr visitUnary_expression(Unary_expressionContext ctx) {
		if (ctx.getChildCount() == 1)
			return (visitPostfix_expression((Postfix_expressionContext) ctx
					.getChild(0)));
		else if (ctx.getChildCount() == 4) {
			Type type = visitType_name((Type_nameContext) ctx.getChild(2));
			return (new Sizeof(type));
		} else if (isLeaf(ctx.getChild(0))) {
			Expr expr = visitUnary_expression((Unary_expressionContext) ctx
					.getChild(1));
			TerminalNode now = (TerminalNode) ctx.getChild(0);
			if (now.getSymbol().getType() == tigerParser.Sizeof)
				return (new Prefix(Prefix.OpType.Sizeof, expr));
			else if (now.getSymbol().getText().equals("++"))
				return (new Prefix(Prefix.OpType.Add, expr));
			else if (now.getSymbol().getText().equals("--"))
				return (new Prefix(Prefix.OpType.Sub, expr));
			else
				error(tigerParser.RULE_unary_expression);
		} else {
			Expr expr = visitCast_expression((Cast_expressionContext) ctx
					.getChild(1));
			Prefix.OpType op = visitUnary_operator((Unary_operatorContext) ctx
					.getChild(0));
			return (new Prefix(op, expr));
		}
		return (null);
	}

	@Override
	public Prefix.OpType visitUnary_operator(Unary_operatorContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String op = now.getSymbol().getText();
		if (op.equals("+"))
			return (Prefix.OpType.Positive);
		else if (op.equals("-"))
			return (Prefix.OpType.Negative);
		else if (op.equals("&"))
			return (Prefix.OpType.Address);
		else if (op.equals("*"))
			return (Prefix.OpType.Pointer);
		else if (op.equals("~"))
			return (Prefix.OpType.BitNot);
		else if (op.equals("!"))
			return (Prefix.OpType.LogNot);
		else
			error(tigerParser.RULE_unary_operator);
		return (null);
	}

	@Override
	public Expr visitPostfix_expression(Postfix_expressionContext ctx) {
		Expr ret = visitPrimary_expression((Primary_expressionContext) ctx
				.getChild(0));
		for (int i = 1; i < ctx.getChildCount(); i++) {
			Postfix temp = visitPostfix((PostfixContext) ctx.getChild(i));
			temp.expr = ret;
			ret = temp;
		}
		return (ret);
	}

	@Override
	public Postfix visitPostfix(PostfixContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		if (now.getSymbol().getText().equals("[")) {
			Expr expr = visitExpression((ExpressionContext) ctx.getChild(1));
			return (new Index(expr));
		} else if (now.getSymbol().getText().equals("(")) {
			Para para = new Para();
			if (!isLeaf(ctx.getChild(1)))
				para = visitArguments((ArgumentsContext) ctx.getChild(1));
			return (new Call(para));
		} else if (now.getSymbol().getText().equals(".")) {
			Id id = visitIdentifier((IdentifierContext) ctx.getChild(1));
			return (new Get(Get.GetType.Direct, id));
		} else if (now.getSymbol().getText().equals("->")) {
			Id id = visitIdentifier((IdentifierContext) ctx.getChild(1));
			return (new Get(Get.GetType.Indirect, id));
		} else if (now.getSymbol().getText().equals("++"))
			return (new Postfix(Postfix.OpType.Add));
		else if (now.getSymbol().getText().equals("--"))
			return (new Postfix(Postfix.OpType.Sub));
		else
			error(tigerParser.RULE_postfix);
		return (null);
	}

	@Override
	public Para visitArguments(ArgumentsContext ctx) {
		Para ret = new Para();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isLeaf(ctx.getChild(i)))
				continue;
			ret.para.add(visitAssignment_expression((Assignment_expressionContext) ctx
					.getChild(i)));
		}
		return (ret);
	}

	@Override
	public Expr visitPrimary_expression(Primary_expressionContext ctx) {
		if (ctx.getChildCount() == 3)
			return (visitExpression((ExpressionContext) ctx.getChild(1)));
		ParserRuleContext now = (ParserRuleContext) ctx.getChild(0);
		if (now.getRuleIndex() == tigerParser.RULE_identifier)
			return (new Variable(visitIdentifier((IdentifierContext) now)));
		else if (now.getRuleIndex() == tigerParser.RULE_constant)
			return (visitConstant((ConstantContext) now));
		else if (now.getRuleIndex() == tigerParser.RULE_string)
			return (new Str(visitString((StringContext) now)));
		else
			error(tigerParser.RULE_primary_expression);
		return (null);
	}

	@Override
	public String visitString(StringContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String ret = "", temp = now.getSymbol().getText();
		for (int i = 1; i < temp.length() - 1; i++)
			if (temp.charAt(i) == '\\') {
				ret += change(temp.charAt(i + 1));
				i++;
			} else
				ret += temp.charAt(i);
		return (ret);
	}

	@Override
	public Expr visitConstant(ConstantContext ctx) {
		ParserRuleContext now = (ParserRuleContext) ctx.getChild(0);
		if (now.getRuleIndex() == tigerParser.RULE_character_constant)
			return (new Char(
					visitCharacter_constant((Character_constantContext) now)));
		else if (now.getRuleIndex() == tigerParser.RULE_integer_constant)
			return (new Int(
					visitInteger_constant((Integer_constantContext) now)));
		else
			error(tigerParser.RULE_constant);
		return (null);
	}

	@Override
	public Character visitCharacter_constant(Character_constantContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String temp = now.getSymbol().getText();
		if (temp.charAt(1) == '\\')
			return (change(temp.charAt(2)));
		else
			return (temp.charAt(1));
	}

	@Override
	public Integer visitInteger_constant(Integer_constantContext ctx) {
		TerminalNode now = (TerminalNode) ctx.getChild(0);
		String temp = now.getSymbol().getText();
		int ret = 0;
		if (now.getSymbol().getType() == tigerParser.DEC)
			for (int i = 0; i < temp.length(); i++)
				ret = ret * 10 + temp.charAt(i) - '0';
		else if (now.getSymbol().getType() == tigerParser.OCT)
			for (int i = 1; i < temp.length(); i++)
				ret = ret * 8 + temp.charAt(i) - '0';
		else if (now.getSymbol().getType() == tigerParser.HEX)
			for (int i = 2; i < temp.length(); i++) {
				char here = temp.charAt(i);
				if (Character.isDigit(here))
					ret = ret * 16 + here - '0';
				else
					ret = ret * 16 + 10 + Character.toUpperCase(here) - 'A';
			}
		else
			error(tigerParser.RULE_integer_constant);
		return (ret);
	}
}
