package tecnico.ulisboa.pt;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ImplicitFlowTest {

    @Test
    public void test1() throws IOException {
        Lattice lattice = new Lattice(new File("target/test-classes/tests/lattice1.txt"));
        ASTParser ast = new ASTParser(lattice, "target/test-classes/tests/ImplicitTest.java");

        String fileContent = new String(Files.readAllBytes(Paths.get("target/test-classes/results/result_implicit.java")));
        
        assertEquals(ast.toString().trim().replaceAll("\\s+", " "), fileContent.trim().replaceAll("\\s+", " "));
    }

    @Test
    public void test2() throws IOException {
        Lattice lattice = new Lattice(new File("target/test-classes/tests/lattice2.txt"));
        ASTParser ast = new ASTParser(lattice, "target/test-classes/tests/ImplicitTest1.java");

        String fileContent = new String(Files.readAllBytes(Paths.get("target/test-classes/results/result_implicit1.java")));
        
        assertEquals(ast.toString().trim().replaceAll("\\s+", " "), fileContent.trim().replaceAll("\\s+", " "));
    }

    @Test
    public void test3() throws IOException {
        Lattice lattice = new Lattice(new File("target/test-classes/tests/lattice2.txt"));
        ASTParser ast = new ASTParser(lattice, "target/test-classes/tests/ImplicitTest2.java");

        String fileContent = new String(Files.readAllBytes(Paths.get("target/test-classes/results/result_implicit2.java")));
        
        assertEquals(ast.toString().trim().replaceAll("\\s+", " "), fileContent.trim().replaceAll("\\s+", " "));
    }
}