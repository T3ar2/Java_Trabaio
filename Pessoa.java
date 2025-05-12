import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

public class Pessoa {
    private int id_pessoa;
    private String nome;
    private String tipo_pessoa;

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


    public void Cadastro_Cliente(){ //Cadastro do cliente no arquivo "OutPut.txt".
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira o ID do Cliente: ");
        setId_pessoa(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Insira o Nome do Cliente:");
        setNome(scanner.nextLine());
        scanner.nextLine(); //Tive que usar um novo scanner por que o java estava imprimindo o Tipo pessoa em cima do SetNome.
        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        setTipo_pessoa(scanner.nextLine());

    }
    public void ImprimirCadastro(){ //Impressão do cliente no arquivo "OutPut.txt".
            try (PrintWriter printwriter = new PrintWriter("Output.txt")) {
                printwriter.println("Id: " + id_pessoa + ";" + "Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ".");
            } catch (IOException e) {
                System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            }
    }
    public void ExclusãoCliente(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecione o Id do Cliente em que deseje apagar: ");
        int selecao = scanner.nextInt();
        if (selecao == id_pessoa){
            id_pessoa = 0;
            nome = "";
            tipo_pessoa = "";
            try (PrintWriter printwriter = new PrintWriter("Output.txt")) {
                printwriter.println("Id: " + id_pessoa + ";" + "Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ".");
            } catch (IOException e) {
                System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            }
        }

    }
}
