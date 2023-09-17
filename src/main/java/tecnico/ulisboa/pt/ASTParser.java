package tecnico.ulisboa.pt;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.javaparser.ast.expr.AssignExpr.Operator.ASSIGN;

public class ASTParser extends ModifierVisitor<Void> {
    private Lattice lattice;
    public CompilationUnit cu;
    private String combination;
    private HashMap<String, String> declassification_variables = new HashMap<>();
    private Stack<String> declassification_stack = new Stack<>();
    private Integer count_declassification;

    public ASTParser(Lattice l, String filename, String combination) {
        this.lattice = l;
        this.combination = combination;

        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(Main.class).resolve("src/main/resources"));
        this.cu = sourceRoot.parse("", filename);
        
        this.count_declassification = 0;
        this.LevelsToClassStructure();
        this.addLiteralVariableDeclarator();
        this.visit(cu, null);
    }

    @Override
    public Visitable visit(LineComment c, Void arg) {
        String regex = "declassification\\(([^,]+),([^,]+)\\)\\s*\\{";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(c.getContent());

        if (this.lattice.getMatrix().containsKey(c.getContent())) {
            this.variableDeclarationRewrite((VariableDeclarationExpr) c.getCommentedNode().get().getChildNodes().get(0), c.getContent());
        } else if (matcher.matches()) {
            String variable = matcher.group(1).trim();
            String new_level = matcher.group(2).trim();

            this.count_declassification = this.count_declassification + 1;
            this.declassification_variables.put(variable + this.count_declassification + "_level", variable);
            this.declassification_stack.push(variable + this.count_declassification + "_level");
            
            VariableDeclarationExpr declaration = new VariableDeclarationExpr(new ClassOrInterfaceType(new_level),
                    variable + this.count_declassification + "_level");

            String statement = "new " + new_level + "Level()";
            
            declaration.getVariable(0).setInitializer(statement);
            ExpressionStmt newStmt = new ExpressionStmt(declaration);

            BlockStmt block = c.getCommentedNode().get().findAncestor(BlockStmt.class).get();
            int count = 0;
            BlockStmt newBlock = new BlockStmt();
            newBlock.copyStatements(block);

            for (Statement st : newBlock.getStatements()) {
                if (st.equals(c.getCommentedNode().get())) {
                    block.addStatement(count, newStmt);
                    block.getStatement(count+2).findAll(NameExpr.class).forEach(n -> {
                        if (n.getNameAsString().contains(variable)) {
                            n.setName(variable + this.count_declassification + "_level");
                        }    
                    });
                }
                count = count + 1;
            }

        } else if (c.getContent().equals("}")) {
            String declassification_variable = this.declassification_stack.pop();
            String variable = this.declassification_variables.get(declassification_variable);
            
            if (c.getCommentedNode().get().getClass().toString().equals(IfStmt.class.toString())) {
                
                c.getCommentedNode().get().findAll(NameExpr.class).forEach(n -> {
                            int flag = 0;
                            if (n.getNameAsString().equals(declassification_variable)) {
                                for (int i = this.count_declassification; i > 0; i--) {
                                    if (this.declassification_stack.search(variable + "_" + i) != -1) {
                                        n.setName(variable + i + "_level");
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    n.setName(variable + "_level");
                                }
                                this.declassification_variables.remove(declassification_variable);
                            }
                        });

            } else {
                
                BlockStmt block = c.getCommentedNode().get().findAncestor(BlockStmt.class).get();
                int count = 0;
                BlockStmt newBlock = new BlockStmt();
                newBlock.copyStatements(block);

                for (Statement st : newBlock.getStatements()) {
                    if (st.equals(c.getCommentedNode().get())) {
                        block.getStatement(count+1).findAll(NameExpr.class).forEach(n -> {
                            int flag = 0;
                            if (n.getNameAsString().equals(declassification_variable)) {
                                for (int i = this.count_declassification; i > 0; i--) {
                                    if (this.declassification_stack.search(variable + "_" + i) != -1) {
                                        n.setName(variable + i + "_level");
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    n.setName(variable + "_level");
                                }
                                this.declassification_variables.remove(declassification_variable);
                            }
                        });
                    }
                    count = count + 1;
                }

            }
        }
        super.visit(c, arg);
        return c;
    }

    @Override
    public Visitable visit(AssignExpr a, Void arg) {
        if (a.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString().equals("Application_Linear")) {
            this.assignmentExprRewrite(a);
        }
        super.visit(a, arg);
        return a;
    }

    @Override
    public Visitable visit(IfStmt a, Void arg) {
        if (a.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString().equals("Application_Linear")) {
            this.ifStmtRewrite(a, arg);
        }
        super.visit(a, arg);
        return a;
    }

    private void addMethods(ClassOrInterfaceDeclaration node, NodeList<MethodDeclaration> methods) {
        for (MethodDeclaration method : methods) {
            MethodDeclaration addedMethod = node.addMethod(method.getNameAsString());
            addedMethod.setModifiers(method.getModifiers());
            addedMethod.setType(method.getType());
            addedMethod.setParameters(method.getParameters());
            addedMethod.setBody(method.getBody().get());
        }
    }

    private NodeList<MethodDeclaration> combination(String class_name, String class_level, String combination, Boolean Interface) {
        NodeList<MethodDeclaration> methods = new NodeList<>();

        Map<String, List<String>> Matrix = this.lattice.getMatrix();

        for (String level : Matrix.keySet()) {
            MethodDeclaration method = new MethodDeclaration();
            method.setName("combine");
            method.setModifiers(Modifier.Keyword.PUBLIC);

            String type = "";
            String parameter = "";
            String result = "";
            if (combination.equals("meet")) {
                result = lattice.meet(class_level, level);
            } else {
                result = lattice.join(class_level, level);
            }

            type = result;
            parameter = level;

            method.setType(new TypeParameter(type));
            method.setParameters(new NodeList<Parameter>(new Parameter(new TypeParameter(parameter), "x")));

            BlockStmt body = new BlockStmt();

            if(!Interface) {
                String statement = "return ";

                if (!result.equals(class_level)) {
                    statement = statement + "new " + type + "Level()";
                } else {
                    statement = statement + "this";
                }

                statement = statement + ";";
                body.addStatement(statement);
                method.setBody(body);
            }

            methods.add(method);
        }

        return methods;
    }

    private void LevelsToClassStructure() {
        Map<String, List<String>> Matrix = this.lattice.getMatrix();
        ArrayList<ClassOrInterfaceDeclaration> level_classes = new ArrayList<>();
        ArrayList<ClassOrInterfaceDeclaration> level_Interfaces = new ArrayList<>();

        for (String level : Matrix.keySet()) {
            ClassOrInterfaceDeclaration newInterface = new ClassOrInterfaceDeclaration();
            ClassOrInterfaceDeclaration newClass = new ClassOrInterfaceDeclaration();
            ConstructorDeclaration newConstructor = newClass.addConstructor(Modifier.Keyword.PUBLIC);
            newInterface.setInterface(true);
            newClass.setInterface(false);
            newInterface.setName(level);
            newClass.setName(level + "Level");

            newConstructor.setName(level + "Level");

            NodeList<ClassOrInterfaceType> inheritance = new NodeList<>();
            for (String inheritedLevel : Matrix.get(level)) {
                inheritance.add(new ClassOrInterfaceType(inheritedLevel));
            }

            this.addMethods(newInterface, this.combination(level, level, this.combination, true));
            this.addMethods(newClass, this.combination(level, level, this.combination, false));

            newInterface.setExtendedTypes(inheritance);
            newClass.setImplementedTypes(new NodeList<ClassOrInterfaceType>(new ClassOrInterfaceType(level)));

            level_Interfaces.add(newInterface);
            level_classes.add(newClass);
        }

        for (int i = 0; i < level_Interfaces.size(); i++) {
            if(i == 0) {
                cu.getTypes().addFirst(level_Interfaces.get(i));
            } else {
                cu.getTypes().addAfter(level_Interfaces.get(i), level_Interfaces.get(i-1));
            }
        }

        for (int i = 0; i < level_classes.size(); i++) {
            if(i == 0) {
                cu.getTypes().addAfter(level_classes.get(i), level_Interfaces.get(level_Interfaces.size()-1));
            } else {
                cu.getTypes().addAfter(level_classes.get(i), level_classes.get(i-1));
            }
        }
    }

    private void addLiteralVariableDeclarator() {
        MethodDeclaration main = cu.getClassByName("Application_Linear").get().getMethodsByName("main").get(0);
        BlockStmt block = main.getBody().get();

        VariableDeclarationExpr declaration = new VariableDeclarationExpr(new ClassOrInterfaceType(this.lattice.getBot()), "literal_level");
        declaration.getVariable(0).setInitializer("new "+ this.lattice.getBot() + "Level()");
        ExpressionStmt newStmt = new ExpressionStmt(declaration);

        block.addStatement(0, newStmt);
    }
    private void variableDeclarationRewrite(VariableDeclarationExpr expr, String level) {
        VariableDeclarator variable = (VariableDeclarator) expr.getChildNodes().get(0);

        VariableDeclarationExpr declaration = new VariableDeclarationExpr(new ClassOrInterfaceType(level), variable.getName().toString() + "_level");
        declaration.getVariable(0).setInitializer("new "+ level + "Level()");
        ExpressionStmt newStmt = new ExpressionStmt(declaration);

        BlockStmt block = expr.findAncestor(BlockStmt.class).get();
        int index = 0;
        for (Statement s : block.getStatements()) {
            if (s.isExpressionStmt()) {
                if (s.asExpressionStmt().getExpression().toString().equals(expr.toString())) {
                    break;
                }
            }
            index = index + 1;
        }
        block.addStatement(index+1, newStmt);
    }

    private void createMethodExpression(ArrayList<Expression> variables, MethodCallExpr valueExpr, int index, boolean literal) {
        MethodCallExpr newExpr;
        NameExpr variable = new NameExpr();

        int flag = 0;
        for (int i = this.count_declassification; i > 0; i--) {
            if (this.declassification_stack.search(variables.get(index).toString() + i + "_level") != -1) {
                variable = new NameExpr(variables.get(index).toString() + i + "_level");
                flag = 1;
                break;
            }
        } if (flag == 0) {
            variable = new NameExpr(variables.get(index).toString() + "_level");
        }

        if (index + 1 < variables.size()) {
            newExpr = new MethodCallExpr();
            newExpr.setScope(variable);
            newExpr.setName("combine");
            valueExpr.setArguments(new NodeList<Expression>(newExpr));
            createMethodExpression(variables, newExpr, index + 1, literal);
        } else {
            if(!literal) {
                valueExpr.setArguments(new NodeList<Expression>(variable));
            } else {
                newExpr = new MethodCallExpr(variable,"combine");
                newExpr.setArguments(new NodeList<Expression>(new NameExpr("literal_level")));
                valueExpr.setArguments(new NodeList<Expression>(newExpr));
            }
        }
    }

    private void assignmentExprRewrite(AssignExpr expr) {
        ArrayList<Expression> variables = new ArrayList<>();

        if (expr.getTarget().toString().contains("_level")) {
            return;
        }

        ExpressionStmt newStmt = new ExpressionStmt();
        if (!expr.getValue().getClass().getSimpleName().equals("NameExpr") && !expr.getValue().getClass().getSimpleName().contains("LiteralExpr")) {
            variables.addAll(expr.getValue().findAll(NameExpr.class));
            MethodCallExpr valueExpr = new MethodCallExpr();
            valueExpr.setName("combine");

            int flag = 0;
            for (int i = this.count_declassification; i > 0; i--) {
                if (this.declassification_stack.search(variables.get(0).toString() + i + "_level") != -1) {
                    valueExpr.setScope(new NameExpr(variables.get(0).toString() + i + "_level"));
                    flag = 1;
                    break;
                }
            } if (flag == 0) {
                valueExpr.setScope(new NameExpr(variables.get(0).toString() + "_level"));
            }

            if (variables.size() > 1) {
                createMethodExpression(variables, valueExpr, 1, expr.getValue().findAll(LiteralExpr.class).size() > 0);
            } else {
                valueExpr.setArguments(new NodeList<Expression>(new NameExpr("literal_level")));
            }
            
            flag = 0;
            for (int i = this.count_declassification; i > 0; i--) {
                if (this.declassification_stack.search(expr.getTarget().toString() + i + "_level") != -1) {
                    newStmt = new ExpressionStmt(new AssignExpr(new NameExpr(expr.getTarget().toString() + i + "_level"), valueExpr, ASSIGN));
                    flag = 1;
                    break;
                }
            } if (flag == 0) {
                newStmt = new ExpressionStmt(new AssignExpr(new NameExpr(expr.getTarget().toString() + "_level"), valueExpr, ASSIGN));
            }

        } else {
            int flag = 0;
            NameExpr target = new NameExpr();
            for (int i = this.count_declassification; i > 0; i--) {
                if (this.declassification_stack.search(expr.getTarget().toString() + i + "_level") != -1) {
                    target = new NameExpr(expr.getTarget().toString() + i + "_level");
                    flag = 1;
                    break;
                }
            } if (flag == 0) {
                target = new NameExpr(expr.getTarget().toString() + "_level");
            }

            if(expr.getValue().getClass().getSimpleName().equals("NameExpr")) {

                NameExpr value = new NameExpr();
                flag = 0;
                for (int i = this.count_declassification; i > 0; i--) {
                    if (this.declassification_stack.search(expr.getValue().toString() + i + "_level") != -1) {
                        value = new NameExpr(expr.getValue().toString() + i + "_level");
                        flag = 1;
                        break;
                    }
                } if (flag == 0) {
                    value = new NameExpr(expr.getValue().toString() + "_level");
                }
                newStmt = new ExpressionStmt(new AssignExpr(target, value, ASSIGN));
            } else {
                newStmt = new ExpressionStmt(new AssignExpr(target, new NameExpr("literal_level"), ASSIGN));
            }
        }

        BlockStmt block = expr.findAncestor(BlockStmt.class).get();
        int index = 0;
        for (Statement s : block.getStatements()) {
            if (s.isExpressionStmt()) {
                if (s.asExpressionStmt().getExpression().toString().equals(expr.toString())) {
                    break;
                }
            }
            index = index + 1;
        }
        block.addStatement(index+1, newStmt);
    }

    private void ifStmtRewrite(IfStmt stmt, Void arg) {
        ArrayList<Expression> variables = new ArrayList<>();

        Expression level_condition_expression;
        if (!stmt.getCondition().getClass().getSimpleName().equals("NameExpr") && !stmt.getCondition().getClass().getSimpleName().contains("LiteralExpr")) {
            variables.addAll(stmt.getCondition().findAll(NameExpr.class));
            MethodCallExpr valueExpr = new MethodCallExpr();
            valueExpr.setName("combine");
            
            int flag = 0;
            for (int i = this.count_declassification; i > 0; i--) {
                if (this.declassification_stack.search(variables.get(0).toString() + i + "_level") != -1) {
                    valueExpr.setScope(new NameExpr(variables.get(0).toString() + i + "_level"));
                    flag = 1;
                    break;
                }
            } if (flag == 0) {
                valueExpr.setScope(new NameExpr(variables.get(0).toString() + "_level"));
            }

            if (variables.size() > 1) {
                createMethodExpression(variables, valueExpr, 1, stmt.getCondition().findAll(LiteralExpr.class).size() > 0);
            } else {
                valueExpr.setArguments(new NodeList<Expression>(new NameExpr("literal_level")));
            }

            level_condition_expression = valueExpr;
        } else {
            if(stmt.getCondition().getClass().getSimpleName().equals("NameExpr")) {
                int flag = 0;
                int aux = 0;
                for (int i = this.count_declassification; i > 0; i--) {
                    if (this.declassification_stack.search(stmt.getCondition().toString() + i + "_level") != -1) {
                        aux = i;
                        flag = 1;
                        break;
                    }
                } if (flag == 0) {
                    level_condition_expression = new NameExpr(stmt.getCondition().toString() + "_level");
                } else {
                    level_condition_expression = new NameExpr(stmt.getCondition().toString() + aux + "_level");
                }
            } else {
                level_condition_expression = new NameExpr("literal_level");
            }
        }

        BlockStmt thenStmt = (BlockStmt) stmt.getThenStmt();

        if (stmt.hasElseBlock()) {
            BlockStmt elseStmt = (BlockStmt) stmt.getElseStmt().get();
            elseStmt.findAll(AssignExpr.class).forEach(expr -> {
                    int flag = 0;
                    NameExpr target = new NameExpr();
                    for (int i = this.count_declassification; i > 0; i--) {
                        if (this.declassification_stack.search(expr.getTarget().toString() + i + "_level") != -1) {
                            target = new NameExpr(expr.getTarget().toString() + i + "_level");
                            flag = 1;
                            break;
                        }
                    } if (flag == 0) {
                        target = new NameExpr(expr.getTarget().toString() + "_level");
                    } 
                    elseStmt.addStatement(0, new ExpressionStmt(new AssignExpr(
                    target, level_condition_expression, ASSIGN)));
            });
        }

        thenStmt.findAll(AssignExpr.class).forEach(expr -> {
                    int flag = 0;
                    NameExpr target = new NameExpr();
                    for (int i = this.count_declassification; i > 0; i--) {
                        if (this.declassification_stack.search(expr.getTarget().toString() + i + "_level") != -1) {
                            target = new NameExpr(expr.getTarget().toString() + i + "_level");
                            flag = 1;
                            break;
                        }
                    } if (flag == 0) {
                        target = new NameExpr(expr.getTarget().toString() + "_level");
                    } 
                    thenStmt.addStatement(0, new ExpressionStmt(new AssignExpr(
                    target, level_condition_expression, ASSIGN)));
            });
    }

    public String toString() {
        /*YamlPrinter printer = new YamlPrinter(true);
        return printer.output(cu);*/
        return cu.toString();
    }
}
