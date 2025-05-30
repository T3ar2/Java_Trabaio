import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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

        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        do {

            System.out.print("iD do Produto: ");
            int num = scannerInt.nextInt();
            if (num > 0 && num < 999999) {
                setIdProduto(num);
                Confirmar();
                setVerificadorIdProduto(1);
            } else
                System.out.println("Id inserido incorretamente.Por favor, insira um número válido.");
        }
        while (verificadorIdProduto != 1);

        System.out.print("Nome do produto: ");
        setNomeProduto(scannerString.nextLine());
        Confirmar();

        System.out.print("Descrição: ");
        setDescricao(scannerString.nextLine());
        Confirmar();

        do {
            System.out.print("Custo: R$");
            double preco = scanner.nextDouble();
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
        scannerInt.close();
        scannerString.close();
    }

    @Override
    public void ImprimirCadastro(){
        try (FileWriter fileWriter = new FileWriter("OutputProduto.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Id: " + idProduto + "Nome: " + nomeProduto + ";" +"Descrição: " + descricao + ";" + "Custo: R$" + custo + ";" + "Preço de Venda: R$" + precoVenda);
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
}
