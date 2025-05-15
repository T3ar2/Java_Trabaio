import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Pessoa {
    private int id_pessoa;
    private String nome;
    private String tipo_pessoa;
    protected boolean positivo;

    Scanner scanner = new Scanner(System.in);
    public int getId_pessoa() {
        return id_pessoa;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo_pessoa() {
        return tipo_pessoa;
    }

    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo_pessoa(String tipo_pessoa) {
        this.tipo_pessoa = tipo_pessoa;
    }

    public void setPositivo(boolean positivo) {
        this.positivo = positivo;
    }

    public void Cadastro_Cliente(){ //Cadastro do cliente no arquivo "OutPut.txt".
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira o ID do Cliente: ");
        setId_pessoa(scanner.nextInt());
        System.out.println("Insira o Nome do Cliente:");
        setNome(scanner.nextLine());
        scanner.nextLine();
       // scanner.nextLine(); Tive que usar um novo scanner por que o java estava imprimindo o Tipo pessoa em cima do SetNome.
        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        setTipo_pessoa(scanner.nextLine());
           /* if ((verificador_String == "Cliente") || (verificador_String == "Fornecedor") || (verificador_String == "Ambos") || (verificador_String == "cliente") || (verificador_String == "fornecedor") || (verificador_String == "ambos")){*/
    }

    public void ImprimirCadastro(){ //Impressão do cliente no arquivo "OutPut.txt".
        try (FileWriter fileWriter = new FileWriter("Output.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + id_pessoa + ";" + "Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ".");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void GravarCadastroLog(){
        System.out.println(nome + " Cadastrado com sucesso, verifique o arquivo Output para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(nome + " Cadastrado com sucesso, verifique o arquivo Output para visualizar seu cadastro. ");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

}
