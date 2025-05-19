import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        scanner.nextLine();
        System.out.println("Insira o Nome do Cliente:");
        setNome(scanner.nextLine());
        scanner.nextLine(); /*Tive que usar um novo scanner por que o java estava imprimindo o Tipo pessoa em cima do SetNome*/
        System.out.println("Insira o Tipo da Pessoa (Cliente, Fornecedor ou ambos):");
        setTipo_pessoa(scanner.nextLine());
/*            if ((tipo_pessoa == "Cliente") || (tipo_pessoa == "Fornecedor") || (tipo_pessoa == "Ambos") || (tipo_pessoa == "cliente") || (tipo_pessoa == "fornecedor") || (tipo_pessoa == "ambos")){
            }*/
    }

    public void ImprimirCadastro(){ //Impressão do cliente no arquivo "OutPut.txt".
        try (FileWriter fileWriter = new FileWriter("Output.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + id_pessoa + ";" +"Nome: " + nome + ";" + "Tipo: " + tipo_pessoa + ".");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void GravarCadastroLog(){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println( nome + "Cadastrado com sucesso, verifique o arquivo Output para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("["+ timestamp + "] " + nome + " Cadastrado com sucesso, verifique o arquivo Output para visualizar seu cadastro. ");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void AtualizarCadastroCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente a ser atualizado: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine(); // limpar o buffer

        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Output.txt"))) {
            StringBuilder novoConteudo = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Cadastro atual: " + linha);

                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();

                    System.out.print("Novo tipo de pessoa: ");
                    String novoTipo = scanner.nextLine();

                    String novaLinha = "Id: " + idBusca + ";" + "Nome: " + novoNome + ";" + "Tipo: " + novoTipo + ".";
                    novoConteudo.append(novaLinha).append("\n");
                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Output.txt"))) {
                    writer.write(novoConteudo.toString());
                }

                System.out.println("Cadastro atualizado com sucesso.");

                try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                    logWriter.write( " Cliente com ID " + idBusca + " atualizado com sucesso.");
                    logWriter.newLine();
                }
            } else {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println("["+ timestamp + "] " + "Cliente com ID " + idBusca + " não encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao atualizar cadastro: " + e.getMessage());
        }
    }
    public void ConsultarCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente a serconsultado: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Output.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    System.out.println("Cliente encontrado:");
                    System.out.println(linha);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("O cliente de ID " + idBusca + " não foi encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao consultar cadastro: " + e.getMessage());
        }
    }

}
