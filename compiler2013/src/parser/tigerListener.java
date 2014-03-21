// Generated from tiger.g4 by ANTLR 4.0

package parser;

import org.antlr.v4.runtime.tree.*;

public interface tigerListener extends ParseTreeListener {
	void enterExpression(tigerParser.ExpressionContext ctx);

	void exitExpression(tigerParser.ExpressionContext ctx);

	void enterDeclarator(tigerParser.DeclaratorContext ctx);

	void exitDeclarator(tigerParser.DeclaratorContext ctx);

	void enterAssignment_expression(tigerParser.Assignment_expressionContext ctx);

	void exitAssignment_expression(tigerParser.Assignment_expressionContext ctx);

	void enterMultiplicative_expression(
			tigerParser.Multiplicative_expressionContext ctx);

	void exitMultiplicative_expression(
			tigerParser.Multiplicative_expressionContext ctx);

	void enterJump_statement(tigerParser.Jump_statementContext ctx);

	void exitJump_statement(tigerParser.Jump_statementContext ctx);

	void enterCompound_statement(tigerParser.Compound_statementContext ctx);

	void exitCompound_statement(tigerParser.Compound_statementContext ctx);

	void enterCast_expression(tigerParser.Cast_expressionContext ctx);

	void exitCast_expression(tigerParser.Cast_expressionContext ctx);

	void enterCharacter_constant(tigerParser.Character_constantContext ctx);

	void exitCharacter_constant(tigerParser.Character_constantContext ctx);

	void enterEquality_expression(tigerParser.Equality_expressionContext ctx);

	void exitEquality_expression(tigerParser.Equality_expressionContext ctx);

	void enterParameters(tigerParser.ParametersContext ctx);

	void exitParameters(tigerParser.ParametersContext ctx);

	void enterFunction_definition(tigerParser.Function_definitionContext ctx);

	void exitFunction_definition(tigerParser.Function_definitionContext ctx);

	void enterDeclaration(tigerParser.DeclarationContext ctx);

	void exitDeclaration(tigerParser.DeclarationContext ctx);

	void enterInit_declarator(tigerParser.Init_declaratorContext ctx);

	void exitInit_declarator(tigerParser.Init_declaratorContext ctx);

	void enterShift_operator(tigerParser.Shift_operatorContext ctx);

	void exitShift_operator(tigerParser.Shift_operatorContext ctx);

	void enterExclusive_or_expression(
			tigerParser.Exclusive_or_expressionContext ctx);

	void exitExclusive_or_expression(
			tigerParser.Exclusive_or_expressionContext ctx);

	void enterStatement(tigerParser.StatementContext ctx);

	void exitStatement(tigerParser.StatementContext ctx);

	void enterLogical_and_expression(
			tigerParser.Logical_and_expressionContext ctx);

	void exitLogical_and_expression(
			tigerParser.Logical_and_expressionContext ctx);

	void enterAdditive_expression(tigerParser.Additive_expressionContext ctx);

	void exitAdditive_expression(tigerParser.Additive_expressionContext ctx);

	void enterUnary_operator(tigerParser.Unary_operatorContext ctx);

	void exitUnary_operator(tigerParser.Unary_operatorContext ctx);

	void enterProgram(tigerParser.ProgramContext ctx);

	void exitProgram(tigerParser.ProgramContext ctx);

	void enterShift_expression(tigerParser.Shift_expressionContext ctx);

	void exitShift_expression(tigerParser.Shift_expressionContext ctx);

	void enterLogical_or_expression(tigerParser.Logical_or_expressionContext ctx);

	void exitLogical_or_expression(tigerParser.Logical_or_expressionContext ctx);

	void enterIteration_statement(tigerParser.Iteration_statementContext ctx);

	void exitIteration_statement(tigerParser.Iteration_statementContext ctx);

	void enterType_name(tigerParser.Type_nameContext ctx);

	void exitType_name(tigerParser.Type_nameContext ctx);

	void enterAdditive_operator(tigerParser.Additive_operatorContext ctx);

	void exitAdditive_operator(tigerParser.Additive_operatorContext ctx);

	void enterPlain_declaration(tigerParser.Plain_declarationContext ctx);

	void exitPlain_declaration(tigerParser.Plain_declarationContext ctx);

	void enterIdentifier(tigerParser.IdentifierContext ctx);

	void exitIdentifier(tigerParser.IdentifierContext ctx);

	void enterStruct_or_union(tigerParser.Struct_or_unionContext ctx);

	void exitStruct_or_union(tigerParser.Struct_or_unionContext ctx);

	void enterInclusive_or_expression(
			tigerParser.Inclusive_or_expressionContext ctx);

	void exitInclusive_or_expression(
			tigerParser.Inclusive_or_expressionContext ctx);

	void enterConstant_expression(tigerParser.Constant_expressionContext ctx);

	void exitConstant_expression(tigerParser.Constant_expressionContext ctx);

	void enterMultiplicative_operator(
			tigerParser.Multiplicative_operatorContext ctx);

	void exitMultiplicative_operator(
			tigerParser.Multiplicative_operatorContext ctx);

	void enterEquality_operator(tigerParser.Equality_operatorContext ctx);

	void exitEquality_operator(tigerParser.Equality_operatorContext ctx);

	void enterRelational_expression(tigerParser.Relational_expressionContext ctx);

	void exitRelational_expression(tigerParser.Relational_expressionContext ctx);

	void enterPostfix_expression(tigerParser.Postfix_expressionContext ctx);

	void exitPostfix_expression(tigerParser.Postfix_expressionContext ctx);

	void enterAssignment_operator(tigerParser.Assignment_operatorContext ctx);

	void exitAssignment_operator(tigerParser.Assignment_operatorContext ctx);

	void enterDeclarators(tigerParser.DeclaratorsContext ctx);

	void exitDeclarators(tigerParser.DeclaratorsContext ctx);

	void enterUnary_expression(tigerParser.Unary_expressionContext ctx);

	void exitUnary_expression(tigerParser.Unary_expressionContext ctx);

	void enterConstant(tigerParser.ConstantContext ctx);

	void exitConstant(tigerParser.ConstantContext ctx);

	void enterSelection_statement(tigerParser.Selection_statementContext ctx);

	void exitSelection_statement(tigerParser.Selection_statementContext ctx);

	void enterExpression_statement(tigerParser.Expression_statementContext ctx);

	void exitExpression_statement(tigerParser.Expression_statementContext ctx);

	void enterPlain_declarator(tigerParser.Plain_declaratorContext ctx);

	void exitPlain_declarator(tigerParser.Plain_declaratorContext ctx);

	void enterInit_declarators(tigerParser.Init_declaratorsContext ctx);

	void exitInit_declarators(tigerParser.Init_declaratorsContext ctx);

	void enterRelational_operator(tigerParser.Relational_operatorContext ctx);

	void exitRelational_operator(tigerParser.Relational_operatorContext ctx);

	void enterPostfix(tigerParser.PostfixContext ctx);

	void exitPostfix(tigerParser.PostfixContext ctx);

	void enterArguments(tigerParser.ArgumentsContext ctx);

	void exitArguments(tigerParser.ArgumentsContext ctx);

	void enterTypedef_name(tigerParser.Typedef_nameContext ctx);

	void exitTypedef_name(tigerParser.Typedef_nameContext ctx);

	void enterInteger_constant(tigerParser.Integer_constantContext ctx);

	void exitInteger_constant(tigerParser.Integer_constantContext ctx);

	void enterAnd_expression(tigerParser.And_expressionContext ctx);

	void exitAnd_expression(tigerParser.And_expressionContext ctx);

	void enterPrimary_expression(tigerParser.Primary_expressionContext ctx);

	void exitPrimary_expression(tigerParser.Primary_expressionContext ctx);

	void enterString(tigerParser.StringContext ctx);

	void exitString(tigerParser.StringContext ctx);

	void enterType_specifier(tigerParser.Type_specifierContext ctx);

	void exitType_specifier(tigerParser.Type_specifierContext ctx);

	void enterInitializer(tigerParser.InitializerContext ctx);

	void exitInitializer(tigerParser.InitializerContext ctx);
}