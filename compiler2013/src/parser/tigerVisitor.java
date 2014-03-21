// Generated from tiger.g4 by ANTLR 4.0

package parser;

import org.antlr.v4.runtime.tree.*;

public interface tigerVisitor<T> extends ParseTreeVisitor<T> {
	T visitExpression(tigerParser.ExpressionContext ctx);

	T visitDeclarator(tigerParser.DeclaratorContext ctx);

	T visitAssignment_expression(tigerParser.Assignment_expressionContext ctx);

	T visitMultiplicative_expression(
			tigerParser.Multiplicative_expressionContext ctx);

	T visitJump_statement(tigerParser.Jump_statementContext ctx);

	T visitCompound_statement(tigerParser.Compound_statementContext ctx);

	T visitCast_expression(tigerParser.Cast_expressionContext ctx);

	T visitCharacter_constant(tigerParser.Character_constantContext ctx);

	T visitEquality_expression(tigerParser.Equality_expressionContext ctx);

	T visitParameters(tigerParser.ParametersContext ctx);

	T visitFunction_definition(tigerParser.Function_definitionContext ctx);

	T visitDeclaration(tigerParser.DeclarationContext ctx);

	T visitInit_declarator(tigerParser.Init_declaratorContext ctx);

	T visitShift_operator(tigerParser.Shift_operatorContext ctx);

	T visitExclusive_or_expression(
			tigerParser.Exclusive_or_expressionContext ctx);

	T visitStatement(tigerParser.StatementContext ctx);

	T visitLogical_and_expression(tigerParser.Logical_and_expressionContext ctx);

	T visitAdditive_expression(tigerParser.Additive_expressionContext ctx);

	T visitUnary_operator(tigerParser.Unary_operatorContext ctx);

	T visitProgram(tigerParser.ProgramContext ctx);

	T visitShift_expression(tigerParser.Shift_expressionContext ctx);

	T visitLogical_or_expression(tigerParser.Logical_or_expressionContext ctx);

	T visitIteration_statement(tigerParser.Iteration_statementContext ctx);

	T visitType_name(tigerParser.Type_nameContext ctx);

	T visitAdditive_operator(tigerParser.Additive_operatorContext ctx);

	T visitPlain_declaration(tigerParser.Plain_declarationContext ctx);

	T visitIdentifier(tigerParser.IdentifierContext ctx);

	T visitStruct_or_union(tigerParser.Struct_or_unionContext ctx);

	T visitInclusive_or_expression(
			tigerParser.Inclusive_or_expressionContext ctx);

	T visitConstant_expression(tigerParser.Constant_expressionContext ctx);

	T visitMultiplicative_operator(
			tigerParser.Multiplicative_operatorContext ctx);

	T visitEquality_operator(tigerParser.Equality_operatorContext ctx);

	T visitRelational_expression(tigerParser.Relational_expressionContext ctx);

	T visitPostfix_expression(tigerParser.Postfix_expressionContext ctx);

	T visitAssignment_operator(tigerParser.Assignment_operatorContext ctx);

	T visitDeclarators(tigerParser.DeclaratorsContext ctx);

	T visitUnary_expression(tigerParser.Unary_expressionContext ctx);

	T visitConstant(tigerParser.ConstantContext ctx);

	T visitSelection_statement(tigerParser.Selection_statementContext ctx);

	T visitExpression_statement(tigerParser.Expression_statementContext ctx);

	T visitPlain_declarator(tigerParser.Plain_declaratorContext ctx);

	T visitInit_declarators(tigerParser.Init_declaratorsContext ctx);

	T visitRelational_operator(tigerParser.Relational_operatorContext ctx);

	T visitPostfix(tigerParser.PostfixContext ctx);

	T visitArguments(tigerParser.ArgumentsContext ctx);

	T visitTypedef_name(tigerParser.Typedef_nameContext ctx);

	T visitInteger_constant(tigerParser.Integer_constantContext ctx);

	T visitAnd_expression(tigerParser.And_expressionContext ctx);

	T visitPrimary_expression(tigerParser.Primary_expressionContext ctx);

	T visitString(tigerParser.StringContext ctx);

	T visitType_specifier(tigerParser.Type_specifierContext ctx);

	T visitInitializer(tigerParser.InitializerContext ctx);
}