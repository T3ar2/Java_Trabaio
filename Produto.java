import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class Produto extends Pessoa{
    private int idProduto;
    private String descricao;
    private double custo;
    private double precoVenda;
    private int verificadorIdProduto;
    private int verificadorCusto;
    private int verificadorPrecoVenda;
    private String nomeProduto;

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public void setVerificadorIdProduto(int verificadorIdProduto) {
        this.verificadorIdProduto = verificadorIdProduto;
    }

    public void setVerificadorCusto(int verificadorCusto) {
        this.verificadorCusto = verificadorCusto;
    }

    public void setVerificadorPrecoVenda(int verificadorPrecoVenda) {
        this.verificadorPrecoVenda = verificadorPrecoVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getCusto() {
        return custo;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void CadastrarProduto(){

        do {

            System.out.print("iD do Produto: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            if (num > 0 && num < 999999) {
                setIdProduto(num);
                Confirmar();
                setVerificadorIdProduto(1);
            } else
                System.out.println("Id inserido incorretamente.Por favor, ensira um número válido.");
        }
        while (verificadorIdProduto != 1);

        System.out.print("Nome do produto: ");
        setNomeProduto(scanner.nextLine());
        Confirmar();

        System.out.print("Descrição: ");
        setDescricao(scanner.nextLine());
        Confirmar();

        do {
            System.out.print("Custo: R$");
            double preco = scanner.nextDouble();
            scanner.nextLine();
            if (preco > 0) {
                setCusto(preco);
                Confirmar();
                setVerificadorCusto(1);
            } else {
                System.out.print("O valor de custo inserido é inválido.");
            }
        }
        while(verificadorCusto != 1);

        do {
            System.out.print("Venda: R$");
            double preco2 = scanner.nextDouble();
            scanner.nextLine();
            if (preco2 > 0) {
                setPrecoVenda(preco2);
                Confirmar();
                setVerificadorPrecoVenda(1);
            } else {
                System.out.println("O valor de custo inserido é inválido.");
            }
        }
        while (verificadorPrecoVenda != 1);
        ImprimirCadastro();
        GravarCadastroLog();
    }

    @Override
    public void ImprimirCadastro(){
        try (FileWriter fileWriter = new FileWriter("OutputProduto.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + idProduto + " Nome: " + nomeProduto + ";" +" Descrição: " + descricao + ";" + " Custo: R$" + custo + ";" + " Preço de Venda: R$" + precoVenda);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    @Override
    public void GravarCadastroLog(){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Produto " + nomeProduto + " Cadastrado com sucesso, verifique o arquivo OutputProduto para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("["+ timestamp + "] " + "usuário admin cadastrou o produto "  + nomeProduto + " no banco de dados. ");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void AlterarProduto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do produto que deseja alterar: ");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;
        StringBuilder novoConteudo = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("OutputProduto.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca)) {
                    encontrado = true;
                    System.out.println("Produto encontrado: " + linha);

                    // Perguntar novo nome
                    System.out.print("Digite o novo nome do produto: ");
                    String novoNome = scanner.nextLine();

                    // Perguntar nova descrição
                    System.out.print("Digite a nova descrição do produto: ");
                    String novaDescricao = scanner.nextLine();

                    // Perguntar novo custo
                    double novoCusto = 0;
                    boolean custoValido = false;
                    do {
                        System.out.print("Digite o novo custo do produto: R$");
                        String input = scanner.nextLine();
                        try {
                            novoCusto = Double.parseDouble(input);
                            if (novoCusto > 0) {
                                custoValido = true;
                            } else {
                                System.out.println("Valor inválido, deve ser maior que zero.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida, digite um número válido.");
                        }
                    } while (!custoValido);

                    // Perguntar novo preço de venda
                    double novoPrecoVenda = 0;
                    boolean precoValido = false;
                    do {
                        System.out.print("Digite o novo preço de venda do produto: R$");
                        String input = scanner.nextLine();
                        try {
                            novoPrecoVenda = Double.parseDouble(input);
                            if (novoPrecoVenda > 0) {
                                precoValido = true;
                            } else {
                                System.out.println("Valor inválido, deve ser maior que zero.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida, digite um número válido.");
                        }
                    } while (!precoValido);

                    // Montar nova linha formatada igual ao OutputProduto
                    StringBuilder novaLinha = new StringBuilder();
                    novaLinha.append("Id: ").append(idBusca).append(" ");
                    novaLinha.append("Nome: ").append(novoNome).append(";");
                    novaLinha.append("Descrição: ").append(novaDescricao).append(";");
                    novaLinha.append("Custo: R$").append(String.format("%.2f", novoCusto)).append(";");
                    novaLinha.append("Preço de Venda: R$").append(String.format("%.2f", novoPrecoVenda));

                    novoConteudo.append(novaLinha.toString()).append("\n");

                    // Gravar no log
                    try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("Log.txt", true))) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        logWriter.write("[" + timestamp + "] usuário admin alterou os dados do produto ID " + idBusca + ".");
                        logWriter.newLine();
                    }

                } else {
                    novoConteudo.append(linha).append("\n");
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputProduto.txt"))) {
                    writer.write(novoConteudo.toString());
                }
                System.out.println("Cadastro do produto atualizado com sucesso!");
            } else {
                System.out.println("Produto com ID " + idBusca + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler ou escrever o arquivo: " + e.getMessage());
        }
    }
}
